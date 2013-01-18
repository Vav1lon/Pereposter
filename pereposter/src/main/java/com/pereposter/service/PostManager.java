package com.pereposter.service;

import com.pereposter.entity.internal.SocialNetworkEnum;
import com.pereposter.entity.internal.User;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.service.socialnetwork.SocialNetworkService;
import com.pereposter.social.entity.Post;
import com.pereposter.utils.ServiceHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Transactional(propagation = Propagation.REQUIRED)
public class PostManager {

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private SessionFactory sessionFactory;

    //TODO: временный метод
    public void starter() {

        List<User> users = (List<User>) getSession().createQuery("FROM User u WHERE u.active = true ").list();

        for (User user : users) {

            findAndWriteNewPost(user);

        }

    }

    //TODO: надо сделать много поточность через countDownlatch
    private synchronized void findAndWriteNewPost(User user) {

        ConcurrentHashMap<SocialNetworkEnum, List<Post>> postsMap = new ConcurrentHashMap<SocialNetworkEnum, List<Post>>();

        for (UserSocialAccount account : user.getAccounts()) {

            ConcurrentHashMap map = findNewPosts(account);

            if (map != null) {
                postsMap.putAll(map);
            }

        }


        for (UserSocialAccount account : user.getAccounts()) {

            SocialNetworkEnum currentAccount = account.getSocialNetwork();
            List<Post> posts = postsMap.get(currentAccount);

            if (posts != null) {

                for (UserSocialAccount accountForWritePosts : user.getAccounts()) {

                    if (!accountForWritePosts.getSocialNetwork().equals(currentAccount)) {

                        writeNewPost(posts, accountForWritePosts);

                    }

                }

                checkReadSourcePost(account, posts);

            }

        }

    }

    private void checkReadSourcePost(UserSocialAccount account, List<Post> posts) {
        Post lastPost = findLastDateOriginalPost(posts);

        if (lastPost != null) {
            account.setCreateDateLastPost(lastPost.getCreatedDate());
            account.setLastPostId(lastPost.getId());
        } else {
            //TODO: пишем в лог!!
            throw new IllegalArgumentException("нет даты последнего поста оригинала");
        }
    }

    private void writeNewPost(List<Post> posts, UserSocialAccount accountForWritePosts) {
        SocialNetworkService service = serviceHelper.getService(accountForWritePosts.getSocialNetwork());
        Post lastPost = service.writePosts(accountForWritePosts, posts);

        accountForWritePosts.setCreateDateLastPost(lastPost.getCreatedDate());
        accountForWritePosts.setLastPostId(lastPost.getId());
    }

    private Post findLastDateOriginalPost(List<Post> posts) {
        DateTime lastPostDate = null;
        Post result = null;
        for (Post post : posts) {

            if (lastPostDate == null) {
                lastPostDate = post.getCreatedDate();
                result = post;
            }

            if (lastPostDate.getMillis() < post.getCreatedDate().getMillis()) {
                lastPostDate = post.getCreatedDate();
                result = post;
            }

        }
        return result;
    }

    private ConcurrentHashMap<SocialNetworkEnum, List<Post>> findNewPosts(UserSocialAccount account) {

        List<Post> posts = serviceHelper.getService(account.getSocialNetwork()).findNewPostByOverCreateDate(account);

        ConcurrentHashMap<SocialNetworkEnum, List<Post>> result = null;

        if (posts != null && !posts.isEmpty()) {
            result = new ConcurrentHashMap<SocialNetworkEnum, List<Post>>();
            result.put(account.getSocialNetwork(), posts);
        }

        return result;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
