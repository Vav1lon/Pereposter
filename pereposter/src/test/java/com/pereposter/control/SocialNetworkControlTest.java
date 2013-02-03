package com.pereposter.control;

import com.pereposter.AbstractTest;
import com.pereposter.TestHelper;
import com.pereposter.entity.RestResponse;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.stub.FacebookSocialWebServicesStub;
import com.pereposter.stub.VkontakteSocialWebServicesStub;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SocialNetworkControlTest extends AbstractTest {

    @Autowired
    private SocialNetworkControl socialNetworkControl;

    @Autowired
    private VkontakteSocialWebServicesStub vkontakteSocialService;

    @Autowired
    private FacebookSocialWebServicesStub facebookSocialService;

    @Autowired
    private TestHelper testHelper;

    UserSocialAccount facebookAccountUser1 = null;
    UserSocialAccount vkontakteAccountUser1 = null;
    UserSocialAccount twitterAccountUser1 = null;

    @Before
    public void setUp() {

        super.setUp();

        globalUser1.setAccounts(new HashSet<UserSocialAccount>(Arrays.asList(socialAccountVkontakteEnable1, socialAccountFaceBookEnabled1, socialAccountTwitterEnabled1)));

        globalUser2.setActive(false);

        getSession().saveOrUpdate(globalUser1);
        getSession().saveOrUpdate(globalUser2);


        fillTestUserAccount1(globalUser1.getAccounts(), new DateTime().minusDays(2));
    }

    private void fillTestUserAccount1(Set<UserSocialAccount> accounts, DateTime setCreateDateLastPost) {
        for (UserSocialAccount account : accounts) {

            account.setCreateDateLastPost(setCreateDateLastPost);

            switch (account.getSocialNetwork()) {

                case FACEBOOK:
                    facebookAccountUser1 = account;
                    break;
                case VKONTAKTE:
                    vkontakteAccountUser1 = account;
                    break;
                case TWITTER:
                    twitterAccountUser1 = account;
                    break;
            }
        }
    }

    @Test
    public void initUserNew() {

        for (UserSocialAccount account : globalUser1.getAccounts()) {
            assertNull(account.getCreateDateLastPost());
            assertNull(account.getLastPostId());
            assertNull(account.getSocialUserId());
        }

        for (UserSocialAccount account : globalUser1.getAccounts()) {
            socialNetworkControl.initializationSocialAccount(account.getId().toString());
        }


        for (UserSocialAccount account : globalUser1.getAccounts()) {
            assertNotNull(account.getCreateDateLastPost());
            assertNotNull(account.getLastPostId());
            assertNotNull(account.getSocialUserId());
        }

    }

    @Test
    public void initUserNewReturnRestResponse() {
        RestResponse response = socialNetworkControl.initializationSocialAccount(((Integer) (Integer.MAX_VALUE / 2)).toString());
        assertNotNull(response);
    }

    @Test
    public void initUserNewWhereEnabledTrue() {

        for (UserSocialAccount account : globalUser1.getAccounts()) {
            assertNull(account.getCreateDateLastPost());
            assertNull(account.getLastPostId());
        }

        socialNetworkControl.initializationSocialAccount(globalUser2.getId().toString());

        boolean flag = false;

        for (UserSocialAccount account : globalUser2.getAccounts()) {

            if (!account.isEnabled() && account.getLastPostId() == null && account.getCreateDateLastPost() == null) {
                flag = true;
            }

        }

        assertTrue(flag);

    }


}
