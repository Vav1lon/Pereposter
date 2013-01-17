package com.pereposter.service;

import com.pereposter.control.SocialNetworkControl;
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

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional(propagation = Propagation.REQUIRED)
public class PostManager {

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private SessionFactory sessionFactory;

    public void findAndWriteNewPost() {

        List<User> users = (List<User>) getSession().createQuery("FROM User u WHERE u.active = true ").list();

        for (User user : users) {

            Map<UserSocialAccount, List<Post>> accountPostMap = findNewPostByUser(user);

            if (checkNewPost(accountPostMap)) {
                writeNewPost(user, accountPostMap);
            }
        }
    }

    private void writeNewPost(User user, Map<UserSocialAccount, List<Post>> accountPostMap) {

        String lastPostId = null;

        for (Map.Entry<UserSocialAccount, List<Post>> entry : accountPostMap.entrySet()) {

            if (entry.getValue() != null) {

                for (UserSocialAccount account : user.getAccounts()) {

                    lastPostId = null;

                    if (!entry.getKey().equals(account)) {

                        SocialNetworkService service = serviceHelper.getService(account.getSocialNetwork());

                        for (Post post : entry.getValue()) {
                            lastPostId = service.writePost(account, post);
                        }

                        account.setLastPostId(lastPostId);
                        account.setCreateDateLastPost(new DateTime());
                        getSession().save(account);

                    }
                }
            }
        }

    }

    private Map<UserSocialAccount, List<Post>> findNewPostByUser(User user) {

        Map<UserSocialAccount, List<Post>> result = new HashMap<UserSocialAccount, List<Post>>();

        for (UserSocialAccount account : user.getAccounts()) {
            if (account.isEnabled()) {
                SocialNetworkService service = serviceHelper.getService(account.getSocialNetwork());
                result.put(account, service.findNewPostByOverCreateDate(account));
            }
        }

        return result;
    }

    private boolean checkNewPost(Map<UserSocialAccount, List<Post>> map) {

        boolean result = false;

        for (Map.Entry<UserSocialAccount, List<Post>> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                result = true;
                break;
            }
        }

        return result;

    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
