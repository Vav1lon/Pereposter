package com.pereposter.control;

import com.pereposter.control.social.SocialControl;
import com.pereposter.entity.Post;
import com.pereposter.entity.internal.SocialNetworkEnum;
import com.pereposter.entity.internal.SocialUser;
import com.pereposter.entity.internal.SocialUserAccount;
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
public class PostManagerControl {

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private SessionFactory sessionFactory;

    ConcurrentHashMap<SocialNetworkEnum, List<Post>> postsMap = new ConcurrentHashMap<SocialNetworkEnum, List<Post>>();

    //TODO: временный метод
    public void starter() {

        List<SocialUser> socialUsers = (List<SocialUser>) getSession().createQuery("FROM SocialUser u WHERE u.active = true ").list();

        for (SocialUser socialUser : socialUsers) {

            findAndWriteNewPost(socialUser);

        }

    }

    //TODO: надо сделать много поточность через countDownlatch
    private synchronized void findAndWriteNewPost(SocialUser socialUser) {

        for (SocialUserAccount account : socialUser.getAccounts()) {

            ConcurrentHashMap map = findNewPosts(account);

            if (map != null) {
                postsMap.putAll(map);
            }

        }

        if (postsMap.size() != 0) {

            for (SocialUserAccount account : socialUser.getAccounts()) {

                SocialNetworkEnum currentAccount = account.getSocialNetwork();
                List<Post> posts = postsMap.get(currentAccount);

                if (posts != null) {

                    for (SocialUserAccount accountForWritePosts : socialUser.getAccounts()) {

                        if (accountForWritePosts.getSocialNetwork().getId() != currentAccount.getId()) {

                            writeNewPosts(posts, accountForWritePosts);

                        }

                    }

                    checkReadSourcePost(account, posts);

                }

            }

            //TODO: hack
//            getSession().flush();

            postsMap.clear();
        }

    }

    private void checkReadSourcePost(SocialUserAccount account, List<Post> posts) {
        Post lastPost = findLastDateOriginalPost(posts);

        if (lastPost != null) {
            account.setCreateDateLastPost(lastPost.getCreatedDate());
            account.setLastPostId(lastPost.getId());
        } else {
            //TODO: пишем в лог!!
            throw new IllegalArgumentException("нет даты последнего поста оригинала");
        }
    }

    private void writeNewPosts(List<Post> posts, SocialUserAccount accountForWritePosts) {
        SocialControl service = serviceHelper.getSocialNetworkControl(accountForWritePosts.getSocialNetwork());
        Post lastPost = service.writePosts(accountForWritePosts, posts);

        if (lastPost != null) {
            accountForWritePosts.setCreateDateLastPost(lastPost.getCreatedDate());
            accountForWritePosts.setLastPostId(lastPost.getId());
        }

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

    private ConcurrentHashMap<SocialNetworkEnum, List<Post>> findNewPosts(SocialUserAccount account) {

        List<Post> posts = serviceHelper.getSocialNetworkControl(account.getSocialNetwork()).findNewPostByOverCreateDate(account);

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
