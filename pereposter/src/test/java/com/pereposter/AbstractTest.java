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

    protected User globalUser1;
    protected User globalUser2;

    protected UserSocialAccount socialAccountFaceBookEnabled1;
    protected UserSocialAccount socialAccountFaceBookEnabled2;
    protected UserSocialAccount socialAccountFaceBookDisable;

    protected UserSocialAccount socialAccountVkontakteEnable1;
    protected UserSocialAccount socialAccountVkontakteEnable2;
    protected UserSocialAccount socialAccountVkontakteDisable;

    protected UserSocialAccount socialAccountTwitterEnabled1;
    protected UserSocialAccount socialAccountTwitterEnabled2;
    protected UserSocialAccount socialAccountTwitterDisabled;

    @Before
    public void setUp() {

        socialAccountFaceBookEnabled1 = createUserSocialAccount(true, SocialNetworkEnum.FACEBOOK);
        socialAccountFaceBookEnabled2 = createUserSocialAccount(true, SocialNetworkEnum.FACEBOOK);
        socialAccountFaceBookDisable = createUserSocialAccount(false, SocialNetworkEnum.FACEBOOK);

        socialAccountVkontakteEnable1 = createUserSocialAccount(true, SocialNetworkEnum.VKONTAKTE);
        socialAccountVkontakteEnable2 = createUserSocialAccount(true, SocialNetworkEnum.VKONTAKTE);
        socialAccountVkontakteDisable = createUserSocialAccount(false, SocialNetworkEnum.VKONTAKTE);

        socialAccountTwitterEnabled1 = createUserSocialAccount(true, SocialNetworkEnum.TWITTER);
        socialAccountTwitterEnabled2 = createUserSocialAccount(true, SocialNetworkEnum.TWITTER);
        socialAccountTwitterDisabled = createUserSocialAccount(false, SocialNetworkEnum.TWITTER);

        globalUser1 = createUser("Test-User-1");
        globalUser2 = createUser("Test-User-2");

        getSession().setFlushMode(FlushMode.ALWAYS);

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