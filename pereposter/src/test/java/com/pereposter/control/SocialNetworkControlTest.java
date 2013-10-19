package com.pereposter.control;

import com.pereposter.AbstractTest;
import com.pereposter.TestHelper;
import com.pereposter.entity.RestResponse;
import com.pereposter.entity.internal.SocialUserAccount;
import com.pereposter.stub.FacebookSocialWebServicesStub;
import com.pereposter.stub.VkontakteSocialWebServicesStub;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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

    SocialUserAccount facebookAccountUser1 = null;
    SocialUserAccount vkontakteAccountUser1 = null;
    SocialUserAccount twitterAccountUser1 = null;

    @Before
    public void setUp() {

        super.setUp();

        globalSocialUser1.setAccounts(Arrays.asList(socialAccountVkontakteEnable1, socialAccountFaceBookEnabled1, socialAccountTwitterEnabled1));
        socialAccountVkontakteEnable1.setSocialUser(globalSocialUser1);
        socialAccountFaceBookEnabled1.setSocialUser(globalSocialUser1);
        socialAccountTwitterEnabled1.setSocialUser(globalSocialUser1);

        globalSocialUser2.setActive(false);

        getSession().saveOrUpdate(globalSocialUser1);
        getSession().saveOrUpdate(globalSocialUser2);

        fillTestUserAccount1(globalSocialUser1.getAccounts(), new DateTime().minusDays(2));
        getSession().flush();
    }

    private void fillTestUserAccount1(List<SocialUserAccount> accounts, DateTime setCreateDateLastPost) {
        for (SocialUserAccount account : accounts) {

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

    //TODO: need fix
    @Ignore
    @Test
    public void initUserNew() {

        for (SocialUserAccount account : globalSocialUser1.getAccounts()) {
            assertNull(account.getCreateDateLastPost());
            assertNull(account.getLastPostId());
            assertNull(account.getSocialUserId());
        }

        for (SocialUserAccount account : globalSocialUser1.getAccounts()) {
            socialNetworkControl.initializationSocialAccount(account.getId().toString());
        }


        for (SocialUserAccount account : globalSocialUser1.getAccounts()) {
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

    //TODO: need fix
    @Ignore
    @Test
    public void initUserNewWhereEnabledTrue() {

        for (SocialUserAccount account : globalSocialUser1.getAccounts()) {
            assertNull(account.getCreateDateLastPost());
            assertNull(account.getLastPostId());
        }

        socialNetworkControl.initializationSocialAccount(globalSocialUser2.getId().toString());

        boolean flag = false;

        for (SocialUserAccount account : globalSocialUser2.getAccounts()) {

            if (!account.isEnabled() && account.getLastPostId() == null && account.getCreateDateLastPost() == null) {
                flag = true;
            }

        }

        assertTrue(flag);
    }


}
