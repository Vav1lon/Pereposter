package com.pereposter.control;

import com.pereposter.AbstractTest;
import com.pereposter.TestHelper;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.stub.FacebookConnectorStub;
import com.pereposter.stub.VkontakteConnectorStub;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SocialNetworkControlTest extends AbstractTest {

    @Autowired
    private SocialNetworkControl socialNetworkControl;

    @Autowired
    private FacebookConnectorStub facebookConnectorStub;

    @Autowired
    private VkontakteConnectorStub vkontakteConnectorStub;

    @Autowired
    private TestHelper testHelper;

    @Before
    public void setUp() {

        super.setUp();

        user.setAccounts(Arrays.asList(socialAccountFaceBookEnabledForUser1, socialAccountVkontakteEnableForUser1));
        user2.setAccounts(Arrays.asList(socialAccountFaceBookEnabledForUser2, socialAccountVkontakteEnableForUser2, socialAccountVkontakteDisableForUser2));

        getSession().saveOrUpdate(user);
        getSession().saveOrUpdate(user2);

        facebookConnectorStub.getFacebookPosts().add(testHelper.createPost("Test"));
        vkontakteConnectorStub.getVkontaktePosts().add(testHelper.createPost("Test"));

    }

    @Test
    public void initUserNew() {

        for (UserSocialAccount account : user.getAccounts()) {
            assertNull(account.getCreateDateLastPost());
            assertNull(account.getLastPostId());
            assertNull(account.getSocialUserId());
        }

        socialNetworkControl.initializationUser(user.getId().toString());

        for (UserSocialAccount account : user.getAccounts()) {
            assertNotNull(account.getCreateDateLastPost());
            assertNotNull(account.getLastPostId());
            assertNotNull(account.getSocialUserId());
        }

    }

    @Test
    public void initUserNewWhereEnabledTrue() {

        for (UserSocialAccount account : user.getAccounts()) {
            assertNull(account.getCreateDateLastPost());
            assertNull(account.getLastPostId());
        }

        socialNetworkControl.initializationUser(user2.getId().toString());

        boolean flag = false;

        for (UserSocialAccount account : user2.getAccounts()) {

            if (!account.isEnabled() && account.getLastPostId() == null && account.getCreateDateLastPost() == null) {
                flag = true;
            }

        }

        assertTrue(flag);

    }


}
