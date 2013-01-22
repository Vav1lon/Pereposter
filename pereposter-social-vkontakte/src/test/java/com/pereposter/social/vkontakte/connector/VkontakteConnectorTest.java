package com.pereposter.social.vkontakte.connector;

import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.SocialAuthService;
import com.pereposter.social.vkontakte.AbstractTest;
import com.pereposter.social.vkontakte.entity.AccessToken;
import org.apache.http.client.methods.HttpGet;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.UUID;

public class VkontakteConnectorTest extends AbstractTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private Client client;

    @Autowired
    private VkontakteConnector connector;

    private SocialAuthService authService;

    private AccessToken accessToken;

    @Before
    public void setUp() {

        authService = initAuthService();
        accessToken = initAccessToken();

        Mockito.when(accessTokenService.getNewAccessToken(authService)).thenReturn(accessToken);

    }

    private AccessToken initAccessToken() {
        AccessToken token = new AccessToken();
        token.setAccessToken(UUID.randomUUID().toString());
        token.setExpiresIn(UUID.randomUUID().getMostSignificantBits());
        token.setUserId(UUID.randomUUID().toString());
        return token;
    }

    private SocialAuthService initAuthService() {
        SocialAuthService result = new SocialAuthService();
        result.setUserId(UUID.randomUUID().toString());
        result.setPassword(UUID.randomUUID().toString());
        result.setLogin(UUID.randomUUID().toString());
        return result;
    }


    @Test
    public void testWriteNewPost() throws Exception {

        //https://api.vk.com/method/wall.get?filter=owner&count=1&access_token=
        final HttpGet httpGet = new HttpGet("https://api.vk.com/method/wall.post?message=TestMessage&access_token=" + accessToken.getAccessToken());

        Mockito.when(client.processRequest(Mockito.any(HttpGet.class), Mockito.anyBoolean())).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                HttpGet inputValue = (HttpGet) invocation.getArguments()[0];
                Assert.assertEquals(httpGet.getURI(), inputValue.getURI());
                return readFileToString("json/findLastPost.json");
            }
        });

        PostEntity postEntityReturn = connector.findLastPost(authService);

        PostEntity postEntity = createPostEntity(1358495422L, "75", "пробный пост, надо удалить");

        Assert.assertEquals(postEntity.getMessage(), postEntityReturn.getMessage());
        Assert.assertEquals(postEntity.getId(), postEntityReturn.getId());
        Assert.assertEquals(postEntity.getCreatedDate(), postEntityReturn.getCreatedDate());

    }

    @Test
    @Ignore
    public void testWriteNewPosts() throws Exception {

    }

    @Test
    @Ignore
    public void testFindPostById() throws Exception {

    }

    @Test
    @Ignore
    public void testFindPostsByOverCreatedDate() throws Exception {

    }

    @Test
    public void testFindLastPost() throws Exception {

        //https://api.vk.com/method/wall.get?filter=owner&count=1&access_token=
        final HttpGet httpGet = new HttpGet("https://api.vk.com/method/wall.get?filter=owner&count=1&access_token=" + accessToken.getAccessToken());

        Mockito.when(client.processRequest(Mockito.any(HttpGet.class), Mockito.anyBoolean())).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                HttpGet inputValue = (HttpGet) invocation.getArguments()[0];
                Assert.assertEquals(httpGet.getURI(), inputValue.getURI());
                return readFileToString("json/findLastPost.json");
            }
        });

        PostEntity postEntityReturn = connector.findLastPost(authService);

        PostEntity postEntity = createPostEntity(1358495422L, "75", "пробный пост, надо удалить");

        Assert.assertEquals(postEntity.getMessage(), postEntityReturn.getMessage());
        Assert.assertEquals(postEntity.getId(), postEntityReturn.getId());
        Assert.assertEquals(postEntity.getCreatedDate(), postEntityReturn.getCreatedDate());


    }

    private PostEntity createPostEntity(Long timeStamp, String id, String message) {
        PostEntity result = new PostEntity();
        result.setCreatedDate(new DateTime(new Date(timeStamp * 1000)));
        result.setId(id);
        result.setMessage(message);
        return result;
    }

}
