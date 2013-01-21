package com.pereposter.control;

import com.pereposter.entity.internal.User;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.entity.Post;
import com.pereposter.utils.ServiceHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("socialNetworkControl")
@Transactional(propagation = Propagation.MANDATORY)
public class SocialNetworkControl {

    @Autowired
    private ServiceHelper serviceHelper;

    @Autowired
    private SessionFactory sessionFactory;

    public void initializationUser(String id) {

        User user = (User) getSession().get(User.class, Long.valueOf(id));

        if (user != null) {
            initializationUser(user);
        }

    }

    public void initializationUser(User user) {

        for (UserSocialAccount account : user.getAccounts()) {

            if (account.isEnabled()) {
                com.pereposter.control.social.SocialNetworkControl service = serviceHelper.getSocialNetworkControl(account.getSocialNetwork());
                Post post = service.findLastUserPost(account);

                if (post != null) {
                    account.setLastPostId(post.getId());
                    account.setCreateDateLastPost(post.getCreatedDate());
                    account.setSocialUserId(post.getOwnerId());
                    getSession().saveOrUpdate(user);
                } else {
                    //TODO: пишем в лог что данной учетной записи не существует или пароль не верен
                    //TODO: уведосить юзера что учетная запись не верна
                }
            }
        }
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
