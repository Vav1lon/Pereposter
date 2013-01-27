package com.pereposter.social.facebook.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.SocialAuthEntity;
import com.pereposter.social.facebook.entity.AccessToken;
import com.pereposter.social.facebook.entity.PostFacebook;
import com.pereposter.social.facebook.entity.PostResponse;
import com.pereposter.social.facebook.entity.WritePostResult;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("facebookConnector")
public class FacebookConnector implements SocialNetworkConnector {

    private final static Logger LOGGER = LoggerFactory.getLogger(FacebookConnector.class);

    @Value("${pereposter.social.facebook.fql.findLastPost}")
    private String fqlFindLastPost;

    @Value("${pereposter.social.facebook.fql.findPostById}")
    private String fqlFindPostById;

    @Value("${pereposter.social.facebook.fql.findPostsByOverCreatedDate}")
    private String fqlFindPostsByOverCreatedDate;

    @Value("${pereposter.social.facebook.graph.writePost}")
    private String graphWritePost;

    @Value("${pereposter.social.facebook.url.request.accessTokenParamName}")
    private String accessTokenParamName;

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccessTokenService facebookAccessTokenService;

    private AccessToken getAccessToken(SocialAuthEntity auth) {
        //TODO: если есть время жизни токена то сделать кеш
        return facebookAccessTokenService.getAccessToken(auth);
    }

    @Override
    public String writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {

        String result = null;
        String url = graphWritePost + StringEscapeUtils.unescapeHtml4(postEntity.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth).getAccessToken();
        String json = client.processRequest(new HttpPost(url), false).getBody();

        if (!json.contains("error")) {

            result = readDataFromResponse(json, WritePostResult.class).getId();

        } else {
            //TODO: пишем в логер
            LOGGER.error("Error write new post in FacebookConnector", json);
        }

        return result;
    }

    @Override
    public String writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {

        String result = null;
        String url;
        String json;

        for (PostEntity post : postEntities) {

            url = graphWritePost + StringEscapeUtils.unescapeHtml4(post.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth).getAccessToken();
            json = client.processRequest(new HttpPost(url), false).getBody();

            if (!json.contains("error")) {

                WritePostResult writePostResult = readDataFromResponse(json, WritePostResult.class);
                if (writePostResult != null) {
                    result = writePostResult.getId();
                }

            } else {
                //TODO: пишем в логер
                LOGGER.error("Error write new posts in FacebookConnector", json);
            }

        }

        return result;
    }

    @Override
    public PostEntity findPostById(SocialAuthEntity auth, String postId) {

        PostEntity result = null;

        String url = fqlFindPostById + "%22" + postId + "%22" + accessTokenParamName + getAccessToken(auth).getAccessToken();

        String json = client.processRequest(new HttpGet(url), false).getBody();

        if (!json.contains("error")) {
            PostResponse response = readDataFromResponse(json, PostResponse.class);
            if (response != null && !response.getData().isEmpty()) {
                result = createAndFillPostFromPostResponse(response);
            }
        } else {
            //TODO: пишем в логер
            LOGGER.error("Error find post by id in FacebookConnector", json);
        }

        return result;
    }

    @Override
    public List<PostEntity> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {

        List<PostEntity> result = new ArrayList<PostEntity>();

        HttpGet query = new HttpGet(fqlFindPostsByOverCreatedDate + createdDate.getMillis() / 1000 + accessTokenParamName + getAccessToken(auth).getAccessToken());

        String json = client.processRequest(query, false).getBody();

        if (!json.contains("error")) {
            PostResponse response = readDataFromResponse(json, PostResponse.class);

            for (PostFacebook postFacebook : response.getData()) {
                result.add(createAndFillPostFromPostFacebook(postFacebook));
            }
        } else {
            //TODO: пишем в логер
            LOGGER.error("Error find post by over create date in FacebookConnector", json);
        }

        return result.isEmpty() ? null : result;

    }

    @Override
    public PostEntity findLastPost(SocialAuthEntity auth) {

        HttpGet query = new HttpGet(fqlFindLastPost + accessTokenParamName + getAccessToken(auth).getAccessToken());

        String json = client.processRequest(query, false).getBody();

        PostEntity result = null;

        if (!json.contains("error")) {
            PostFacebook postFacebook = readDataFromResponse(json, PostFacebook.class);
            result = createAndFillPostFromPostFacebook(postFacebook);
        } else {
            //TODO: пишем в логер
            LOGGER.error("Error find last post in FacebookConnector", json);
        }

        return result;
    }

    private PostEntity createAndFillPostFromPostFacebook(PostFacebook post) {
        PostEntity result = new PostEntity();
        result.setId(post.getPost_id());
        result.setMessage(post.getMessage());
        result.setCreatedDate(new DateTime(post.getCreated_time() * 1000));
        return result;
    }

    private PostEntity createAndFillPostFromPostResponse(PostResponse response) {

        PostEntity result = null;

        if (response != null && !response.getData().isEmpty()) {
            PostFacebook postFacebook = response.getData().get(0);
            result = createAndFillPostFromPostFacebook(postFacebook);
        }

        return result;
    }

    private <T> T readDataFromResponse(String json, Class<T> clazz) {

        T result = null;
        if (!Strings.isNullOrEmpty(json)) {
            try {
                result = objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                //TODO: писать в лог онибку!
                LOGGER.error("Ошибка в Facebook connector, при разбора json", e);
            }

        }
        return result;
    }

}
