package com.pereposter;

import com.pereposter.entity.internal.SocialNetworkEnum;
import com.pereposter.entity.internal.User;
import com.pereposter.entity.internal.UserSocialAccount;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml", "/database-context-test.xml"})
public abstract class AbstractTest {

    @Autowired
    private SessionFactory sessionFactory;

    protected User user;
    protected User user2;

    protected UserSocialAccount socialAccountFaceBookEnabledForUser1;
    protected UserSocialAccount socialAccountVkontakteEnableForUser1;

    protected UserSocialAccount socialAccountFaceBookEnabledForUser2;
    protected UserSocialAccount socialAccountVkontakteEnableForUser2;
    protected UserSocialAccount socialAccountVkontakteDisableForUser2;

    @Before
    public void setUp() {

        getSession().setFlushMode(FlushMode.ALWAYS);

        socialAccountFaceBookEnabledForUser1 = createUserSocialAccount(true, SocialNetworkEnum.FACEBOOK);
        socialAccountVkontakteEnableForUser1 = createUserSocialAccount(true, SocialNetworkEnum.VKONTAKTE);

        socialAccountFaceBookEnabledForUser2 = createUserSocialAccount(true, SocialNetworkEnum.FACEBOOK);
        socialAccountVkontakteEnableForUser2 = createUserSocialAccount(true, SocialNetworkEnum.VKONTAKTE);
        socialAccountVkontakteDisableForUser2 = createUserSocialAccount(false, SocialNetworkEnum.VKONTAKTE);

        user = createUser("Test-User-1");
        user2 = createUser("Test-User-2");

    }

    private User createUser(String name) {
        User result = new User();
        result.setName(name);
        result.setActive(true);
        return result;
    }

    private UserSocialAccount createUserSocialAccount(boolean enabled, SocialNetworkEnum networkEnum) {

        UUID uuid = UUID.randomUUID();

        UserSocialAccount account = new UserSocialAccount();
        account.setEnabled(enabled);
        account.setSocialNetwork(networkEnum);
        account.setPassword(uuid.toString());
        account.setUsername("user-" + uuid.toString());
        return account;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}