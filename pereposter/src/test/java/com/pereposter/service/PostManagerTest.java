package com.pereposter.service;

import com.pereposter.AbstractTest;
import com.pereposter.TestHelper;
import com.pereposter.control.PostManagerControl;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.entity.Post;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PostManagerTest extends AbstractTest {

    @Autowired
    private PostManagerControl postManager;

    @Autowired
    private TestHelper testHelper;

    @Before
    public void setUp() {

        super.setUp();

        user.setAccounts(Arrays.asList(socialAccountFaceBookEnabledForUser1, socialAccountVkontakteEnableForUser1));
        user2.setAccounts(Arrays.asList(socialAccountFaceBookEnabledForUser2, socialAccountVkontakteEnableForUser2, socialAccountVkontakteDisableForUser2));

        getSession().saveOrUpdate(user);
        getSession().saveOrUpdate(user2);
    }

    @Test
    public void writeOneToOne() {

        Post post1 = testHelper.createPost("Post==1", new DateTime().minusHours(10));
        Post post2 = testHelper.createPost("Post==2", new DateTime().minusHours(8));

        user2.setActive(false);

        testHelper.setVkontakteSource(true);
        testHelper.setSourcePost(Arrays.asList(post1, post2));

        postManager.starter();


        for (UserSocialAccount account : user.getAccounts()) {

            switch (account.getSocialNetwork()) {

                case VKONTAKTE:

                    assertEquals(post2.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(post2.getId(), account.getLastPostId());

                    break;
                case FACEBOOK:

                    assertEquals(post2.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(post2.getId(), account.getLastPostId());

                    break;

            }

        }

        assertEquals(testHelper.getCountWriteRowToFacebook(), 2);
        assertEquals(testHelper.getCountWriteRowToVkontakte(), 0);
    }


}
