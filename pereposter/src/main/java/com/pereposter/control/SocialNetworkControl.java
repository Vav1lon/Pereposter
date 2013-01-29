package com.pereposter.control;

import com.pereposter.entity.Post;
import com.pereposter.entity.internal.UserSocialAccount;
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

        UserSocialAccount user = (UserSocialAccount) getSession().get(UserSocialAccount.class, Long.valueOf(id));

        if (user != null) {
            initializationUser(user);
        }

    }

    public void initializationUser(UserSocialAccount account) {


        com.pereposter.control.social.SocialNetworkControl service = serviceHelper.getSocialNetworkControl(account.getSocialNetwork());
        Post post = service.findLastUserPost(account);

        if (post != null) {
            account.setLastPostId(post.getId());
            account.setCreateDateLastPost(post.getCreatedDate());
            account.setSocialUserId(post.getOwnerId());
            getSession().saveOrUpdate(account);
        } else {
            //TODO: пишем в лог что данной учетной записи не существует или пароль не верен
            //TODO: уведосить юзера что учетная запись не верна
        }

    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
