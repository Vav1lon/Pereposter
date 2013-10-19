package com.pereposter.control;

import com.pereposter.AbstractTest;
import com.pereposter.TestHelper;
import com.pereposter.control.social.SocialControl;
import com.pereposter.entity.Post;
import com.pereposter.entity.internal.SocialUser;
import com.pereposter.entity.internal.SocialUserAccount;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PostManagerTest extends AbstractTest {

    @Autowired
    private PostManagerControl postManager;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private SocialControl facebookControl;

    @Autowired
    private SocialControl vkontakteControl;

    @Autowired
    private SocialControl twitterControl;

    SocialUserAccount facebookAccountUser1 = null;
    SocialUserAccount vkontakteAccountUser1 = null;
    SocialUserAccount twitterAccountUser1 = null;

    SocialUserAccount facebookAccountUser2 = null;
    SocialUserAccount vkontakteAccountUser2 = null;
    SocialUserAccount twitterAccountUser2 = null;


    @Before
    public void setUp() {

        super.setUp();

        globalSocialUser1.setAccounts(Arrays.asList(socialAccountVkontakteEnable1, socialAccountFaceBookEnabled1, socialAccountTwitterEnabled1));

        globalSocialUser2.setActive(false);

        getSession().saveOrUpdate(globalSocialUser1);
        getSession().saveOrUpdate(globalSocialUser2);


        fillTestUserAccount1(globalSocialUser1.getAccounts(), new DateTime().minusDays(2));

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

    private void fillTestUserAccount2(List<SocialUserAccount> accounts, DateTime setCreateDateLastPost) {
        for (SocialUserAccount account : accounts) {

            account.setCreateDateLastPost(setCreateDateLastPost);

            switch (account.getSocialNetwork()) {

                case FACEBOOK:
                    facebookAccountUser2 = account;
                    break;
                case VKONTAKTE:
                    vkontakteAccountUser2 = account;
                    break;
                case TWITTER:
                    twitterAccountUser2 = account;
                    break;
            }
        }
    }

    @Test
    public void writeOneToOne() {

        // create mock response
        Post post1 = testHelper.createPost("Post==1", new DateTime().minusHours(10));
        Post post2 = testHelper.createPost("Post==2", new DateTime().minusHours(8));
        Post responsePostOnFacebook = testHelper.createPost("Post==3", new DateTime().plusHours(5));

        Mockito.when(facebookControl.findNewPostByOverCreateDate(facebookAccountUser1)).thenReturn(null);
        Mockito.when(vkontakteControl.findNewPostByOverCreateDate(vkontakteAccountUser1)).thenReturn(Arrays.asList(post1, post2));

        Mockito.when(facebookControl.writePosts(facebookAccountUser1, Arrays.asList(post1, post2))).thenReturn(responsePostOnFacebook);


        // test logic
        postManager.starter();


        // verefy result
        Mockito.verify(facebookControl, Mockito.times(1)).findNewPostByOverCreateDate(facebookAccountUser1);
        Mockito.verify(vkontakteControl, Mockito.times(1)).findNewPostByOverCreateDate(vkontakteAccountUser1);

        Mockito.verify(facebookControl, Mockito.times(1)).writePosts(facebookAccountUser1, Arrays.asList(post1, post2));

        SocialUser socialUser = (SocialUser) getSession().get(SocialUser.class, globalSocialUser1.getId());

        for (SocialUserAccount account : socialUser.getAccounts()) {

            switch (account.getSocialNetwork()) {

                case VKONTAKTE:

                    assertEquals(post2.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(post2.getId(), account.getLastPostId());

                    break;
                case FACEBOOK:

                    assertEquals(responsePostOnFacebook.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(responsePostOnFacebook.getId(), account.getLastPostId());

                    break;

            }

        }

    }

    @Test
    public void writeOneToMany() {

        // create mock response
        Post vkontaktePost1 = testHelper.createPost("Post=from=vkontakte1", new DateTime().minusHours(9));
        Post vkontaktePost2 = testHelper.createPost("Post=from=vkontakte2", new DateTime().minusHours(6));
        Post vkontaktePost3 = testHelper.createPost("Post=from=vkontakte3", new DateTime().minusHours(3));
        Post responsePostFacebook = testHelper.createPost("Post=On=Facebook", new DateTime().plusHours(3));
        Post responsePostTwitter = testHelper.createPost("Post=On=Twitter", new DateTime().plusHours(6));


        Mockito.when(facebookControl.findNewPostByOverCreateDate(facebookAccountUser1)).thenReturn(null);
        Mockito.when(twitterControl.findNewPostByOverCreateDate(twitterAccountUser1)).thenReturn(null);

        List<Post> vkontaktePosts = Arrays.asList(vkontaktePost2, vkontaktePost3, vkontaktePost1);

        Mockito.when(vkontakteControl.findNewPostByOverCreateDate(vkontakteAccountUser1)).thenReturn(vkontaktePosts);

        Mockito.when(facebookControl.writePosts(facebookAccountUser1, vkontaktePosts)).thenReturn(responsePostFacebook);
        Mockito.when(twitterControl.writePosts(twitterAccountUser1, vkontaktePosts)).thenReturn(responsePostTwitter);


        // test logic
        postManager.starter();


        // verefy result
        Mockito.verify(facebookControl, Mockito.times(1)).findNewPostByOverCreateDate(facebookAccountUser1);
        Mockito.verify(vkontakteControl, Mockito.times(1)).findNewPostByOverCreateDate(vkontakteAccountUser1);
        Mockito.verify(twitterControl, Mockito.times(1)).findNewPostByOverCreateDate(twitterAccountUser1);

        Mockito.verify(facebookControl, Mockito.times(1)).writePosts(facebookAccountUser1, vkontaktePosts);
        Mockito.verify(twitterControl, Mockito.times(1)).writePosts(twitterAccountUser1, vkontaktePosts);

        SocialUser socialUser = (SocialUser) getSession().get(SocialUser.class, globalSocialUser1.getId());

        for (SocialUserAccount account : socialUser.getAccounts()) {

            switch (account.getSocialNetwork()) {

                case VKONTAKTE:

                    assertEquals(vkontaktePost3.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(vkontaktePost3.getId(), account.getLastPostId());

                    break;
                case FACEBOOK:

                    assertEquals(responsePostFacebook.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(responsePostFacebook.getId(), account.getLastPostId());

                    break;
                case TWITTER:

                    assertEquals(responsePostTwitter.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(responsePostTwitter.getId(), account.getLastPostId());

                    break;

            }

        }
    }

    @Test
    public void writeManyToOne() {
        // create mock response
        Post vkontaktePost1 = testHelper.createPost("Post=from=vkontakte1", new DateTime().minusHours(9));
        Post vkontaktePost2 = testHelper.createPost("Post=from=vkontakte2", new DateTime().minusHours(6));
        Post vkontaktePost3 = testHelper.createPost("Post=from=vkontakte3", new DateTime().minusHours(3));
        Post facebookPost1 = testHelper.createPost("Post=from=facebook1", new DateTime().minusHours(8));
        Post facebookPost2 = testHelper.createPost("Post=from=facebook2", new DateTime().minusHours(4));

        Post responsePostTwitter1 = testHelper.createPost("Post=On=Twitter", new DateTime().plusHours(6));
        Post responsePostTwitter2 = testHelper.createPost("Post=On=Twitter", new DateTime().plusHours(12));
        Post responsePostFacebook = testHelper.createPost("Post=On=Facebook", new DateTime().plusHours(7));
        Post responsePostVkontakte = testHelper.createPost("Post=On=Vkontakte", new DateTime().plusHours(8));


        Mockito.when(twitterControl.findNewPostByOverCreateDate(twitterAccountUser1)).thenReturn(null);

        List<Post> vkontaktePosts = Arrays.asList(vkontaktePost2, vkontaktePost3, vkontaktePost1);
        List<Post> facebookPosts = Arrays.asList(facebookPost2, facebookPost1);

        Mockito.when(vkontakteControl.findNewPostByOverCreateDate(vkontakteAccountUser1)).thenReturn(vkontaktePosts);
        Mockito.when(facebookControl.findNewPostByOverCreateDate(facebookAccountUser1)).thenReturn(facebookPosts);

        Mockito.when(vkontakteControl.writePosts(vkontakteAccountUser1, facebookPosts)).thenReturn(responsePostVkontakte);
        Mockito.when(facebookControl.writePosts(facebookAccountUser1, vkontaktePosts)).thenReturn(responsePostFacebook);
        Mockito.when(twitterControl.writePosts(twitterAccountUser1, vkontaktePosts)).thenReturn(responsePostTwitter1);
        Mockito.when(twitterControl.writePosts(twitterAccountUser1, facebookPosts)).thenReturn(responsePostTwitter2);


        // test logic
        postManager.starter();


        // verefy result
        Mockito.verify(facebookControl, Mockito.times(1)).findNewPostByOverCreateDate(facebookAccountUser1);
        Mockito.verify(vkontakteControl, Mockito.times(1)).findNewPostByOverCreateDate(vkontakteAccountUser1);
        Mockito.verify(twitterControl, Mockito.times(1)).findNewPostByOverCreateDate(twitterAccountUser1);

        Mockito.verify(twitterControl, Mockito.times(1)).writePosts(twitterAccountUser1, facebookPosts);
        Mockito.verify(twitterControl, Mockito.times(1)).writePosts(twitterAccountUser1, vkontaktePosts);
        Mockito.verify(vkontakteControl, Mockito.times(1)).writePosts(vkontakteAccountUser1, facebookPosts);
        Mockito.verify(facebookControl, Mockito.times(1)).writePosts(facebookAccountUser1, vkontaktePosts);

        SocialUser socialUser = (SocialUser) getSession().get(SocialUser.class, globalSocialUser1.getId());

        for (SocialUserAccount account : socialUser.getAccounts()) {

            switch (account.getSocialNetwork()) {

                case VKONTAKTE:

                    assertEquals(responsePostVkontakte.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(responsePostVkontakte.getId(), account.getLastPostId());

                    break;
                case FACEBOOK:

                    assertEquals(facebookPost2.getCreatedDate(), account.getCreateDateLastPost());
                    assertEquals(facebookPost2.getId(), account.getLastPostId());

                    break;
//                case TWITTER:
//
//                    assertEquals(responsePostTwitter2.getCreatedDate(), account.getCreateDateLastPost());
//                    assertEquals(responsePostTwitter2.getId(), account.getLastPostId());
//
//                    break;

            }

        }
    }

}

