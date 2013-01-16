package com.pereposter.service;

import com.pereposter.AbstractTest;
import com.pereposter.entity.internal.UserSocialAccount;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PostManagerTest extends AbstractTest {

    @Autowired
    private PostManager postManager;

    @Test
    public void activePerepost() {

        DateTime tomorrow = new DateTime().minusDays(1);

        for (UserSocialAccount account : user.getAccounts()) {
            account.setCreateDateLastPost(tomorrow);
            account.setLastPostId(Long.toString(Long.MIN_VALUE));
        }
        for (UserSocialAccount account : user2.getAccounts()) {
            account.setCreateDateLastPost(tomorrow);
            account.setLastPostId(Long.toString(Long.MIN_VALUE));
        }

        postManager.findAndWriteNewPost();

        for (UserSocialAccount account : user.getAccounts()) {
            if (account.isEnabled()) {
                assertNotEquals(tomorrow, account.getCreateDateLastPost());
                assertNotEquals(Long.toString(Long.MIN_VALUE), account.getLastPostId());
            }
        }
        for (UserSocialAccount account : user2.getAccounts()) {
            if (account.isEnabled()) {
                assertNotEquals(tomorrow, account.getCreateDateLastPost());
                assertNotEquals(Long.toString(Long.MIN_VALUE), account.getLastPostId());
            }
        }

    }


}
