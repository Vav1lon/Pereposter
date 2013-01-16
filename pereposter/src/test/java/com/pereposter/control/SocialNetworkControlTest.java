package com.pereposter.control;

import com.pereposter.AbstractTest;
import com.pereposter.entity.internal.UserSocialAccount;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class SocialNetworkControlTest extends AbstractTest {

    @Autowired
    private SocialNetworkControl socialNetworkControl;

    @Test
    public void initUserNew() {

        socialNetworkControl.initializationUser(user);
        socialNetworkControl.initializationUser(user2);

        for (UserSocialAccount account : user.getAccounts()) {

            if (account.isEnabled()) {
                assertNotNull(account.getLastPostId());
                assertNotNull(account.getCreateDateLastPost());
            }
        }

        socialNetworkControl.initializationUser(user2);

        for (UserSocialAccount account : user2.getAccounts()) {

            if (account.isEnabled()) {
                assertNotNull(account.getLastPostId());
                assertNotNull(account.getCreateDateLastPost());
            }
        }


    }


}
