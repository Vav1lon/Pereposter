package com.pereposter;

import com.pereposter.entity.internal.SocialNetworkEnum;
import com.pereposter.entity.internal.User;
import com.pereposter.entity.internal.UserSocialAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml", "/database-context-test.xml"})
@Transactional
public abstract class AbstractTest {

    @Autowired
    private SessionFactory sessionFactory;

    private Random random = new Random();

    protected User user;
    protected User user2;

    @Before
    public void setUp() {

        user = createUserTwoNetwork("Test-User-1");
        user2 = createUserThreeNetwork("Test-User-2");

        getSession().save(user);
        getSession().save(user2);

        assert getSession().createQuery("FROM User").list().size() == 2;

    }

    private User createUserTwoNetwork(String name) {

        User result = new User();
        result.setName(name);
        result.setId(random.nextLong());
        result.setActive(true);

        List<UserSocialAccount> userSocialAccounts = new ArrayList<UserSocialAccount>();

        userSocialAccounts.add(createUserSocialAccount(true, SocialNetworkEnum.FACEBOOK));
        userSocialAccounts.add(createUserSocialAccount(true, SocialNetworkEnum.VKONTAKTE));
        userSocialAccounts.add(createUserSocialAccount(false, SocialNetworkEnum.VKONTAKTE));

        result.setAccounts(userSocialAccounts);
        return result;
    }

    private User createUserThreeNetwork(String name) {

        User result = new User();
        result.setName(name);
        result.setId(random.nextLong());
        result.setActive(true);

        List<UserSocialAccount> userSocialAccounts = new ArrayList<UserSocialAccount>();

        userSocialAccounts.add(createUserSocialAccount(true, SocialNetworkEnum.FACEBOOK));
        userSocialAccounts.add(createUserSocialAccount(true, SocialNetworkEnum.VKONTAKTE));
        userSocialAccounts.add(createUserSocialAccount(false, SocialNetworkEnum.VKONTAKTE));
        userSocialAccounts.add(createUserSocialAccount(false, SocialNetworkEnum.FACEBOOK));

        result.setAccounts(userSocialAccounts);
        return result;
    }

    private UserSocialAccount createUserSocialAccount(boolean enabled, SocialNetworkEnum networkEnum) {

        UUID uuid = UUID.randomUUID();

        UserSocialAccount account = new UserSocialAccount();
        account.setEnabled(enabled);
        account.setSocialNetwork(networkEnum);
        account.setPassword(uuid.toString());
        account.setUsername("facebook-user-" + uuid.toString());
        return account;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}