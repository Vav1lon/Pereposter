package com.pereposter;

import com.pereposter.entity.internal.SocialNetworkEnum;
import com.pereposter.entity.internal.SocialUser;
import com.pereposter.entity.internal.SocialUserAccount;
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

    protected SocialUser globalSocialUser1;
    protected SocialUser globalSocialUser2;

    protected SocialUserAccount socialAccountFaceBookEnabled1;
    protected SocialUserAccount socialAccountFaceBookEnabled2;
    protected SocialUserAccount socialAccountFaceBookDisable;

    protected SocialUserAccount socialAccountVkontakteEnable1;
    protected SocialUserAccount socialAccountVkontakteEnable2;
    protected SocialUserAccount socialAccountVkontakteDisable;

    protected SocialUserAccount socialAccountTwitterEnabled1;
    protected SocialUserAccount socialAccountTwitterEnabled2;
    protected SocialUserAccount socialAccountTwitterDisabled;

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

        globalSocialUser1 = createUser("Test-SocialUser-1");
        globalSocialUser2 = createUser("Test-SocialUser-2");

        getSession().setFlushMode(FlushMode.ALWAYS);

    }

    private SocialUser createUser(String name) {
        SocialUser result = new SocialUser();
        result.setName(name);
        result.setActive(true);
        return result;
    }

    private SocialUserAccount createUserSocialAccount(boolean enabled, SocialNetworkEnum networkEnum) {

        UUID uuid = UUID.randomUUID();

        SocialUserAccount account = new SocialUserAccount();
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