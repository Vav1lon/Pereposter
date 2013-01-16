package com.pereposter.control;

import com.pereposter.entity.internal.User;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.service.socialnetwork.SocialNetworkService;
import com.pereposter.social.entity.PostKeyInfo;
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

    public void initializationUser(User user) {

        for (UserSocialAccount account : user.getAccounts()) {

            if (account.isEnabled()) {
                SocialNetworkService service = serviceHelper.getService(account.getSocialNetwork());
                PostKeyInfo postKeyInfo = service.findLastUserPost(account);

                if (postKeyInfo != null) {
                    account.setLastPostId(postKeyInfo.getId());
                    account.setCreateDateLastPost(postKeyInfo.getCreatedDate());
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
