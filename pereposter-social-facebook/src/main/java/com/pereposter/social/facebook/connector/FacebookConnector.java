package com.pereposter.social.facebook.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.FacebookException;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;
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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private ConcurrentHashMap<String, AccessToken> accessTokenMap;

    @PostConstruct
    private void initConnector() {
        accessTokenMap = new ConcurrentHashMap<String, AccessToken>();
    }

    //TODO: проверитьм етод!!
    public synchronized void checkValidToken() {

        long currentTimeStamp = new DateTime().getMillis();

        for (Map.Entry<String, AccessToken> entry : accessTokenMap.entrySet()) {

            if (currentTimeStamp > (entry.getValue().getExpiresIn() * 1000)) {
                accessTokenMap.remove(entry.getKey());
            }

        }

    }

    private String getAccessToken(SocialAuthEntity auth) {

        AccessToken result = accessTokenMap.get(auth.getUserId());

        try {
            if (result == null) {
                result = facebookAccessTokenService.getAccessToken(auth);
                accessTokenMap.put(result.getUserId(), result);
            }
        } catch (Exception e) {
            // TODO: write log
            LOGGER.error("can not access to use for SocialAuthEntity: " + auth.toString());
        }

        return result != null ? result.getAccessToken() : null;
    }

    @Override
    public ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {

        ResponseObject<String> result = new ResponseObject<String>();

        if (Strings.isNullOrEmpty(postEntity.getMessage())) {

            try {

                String json = writePostToWall(auth, postEntity);

                checkErrorInResponse(json);

                result.setValue(readDataFromResponse(json, WritePostResult.class).getId());

            } catch (FacebookException e) {
                LOGGER.error(e.getMessage(), e);
                result.setStatus(ResponseStatus.ERROR);
                result.getErrors().add(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {

        ResponseObject<String> result = new ResponseObject<String>();
        String json;

        try {

            for (PostEntity post : postEntities) {

                json = writePostToWall(auth, post);

                checkErrorInResponse(json);

                WritePostResult writePostResult = readDataFromResponse(json, WritePostResult.class);
                if (writePostResult != null) {
                    result.setValue(writePostResult.getId());
                }


            }
        } catch (FacebookException e) {
            //TODO: пишем в логер
            LOGGER.error(e.getMessage(), e);
            result.setStatus(ResponseStatus.WARNING);
            result.getErrors().add(e.getMessage());

        }

        if (result.getErrors().size() >= postEntities.size()) {
            result.setStatus(ResponseStatus.ERROR);
        }

        return result;
    }

    @Override
    public ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId) {

        ResponseObject<PostEntity> result = new ResponseObject<PostEntity>();

        String url = fqlFindPostById + "%22" + postId + "%22" + accessTokenParamName + getAccessToken(auth);

        try {

            String json = client.processRequest(new HttpGet(url)).getBody();

            checkErrorInResponse(json);

            PostResponse response = readDataFromResponse(json, PostResponse.class);
            if (response != null && !response.getData().isEmpty()) {
                result.setValue(createAndFillPostFromPostResponse(response));
            }

        } catch (FacebookException e) {
            //TODO: пишем в логер
            LOGGER.error(e.getMessage(), e);
            result.setStatus(ResponseStatus.ERROR);
            result.getErrors().add(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {

        ResponseObject<PostsResponse> result = new ResponseObject<PostsResponse>();

        HttpGet query = new HttpGet(fqlFindPostsByOverCreatedDate + createdDate.getMillis() / 1000 + accessTokenParamName + getAccessToken(auth));

        List<PostEntity> value = new ArrayList<PostEntity>();

        try {


            String json = client.processRequest(query).getBody();

            checkErrorInResponse(json);

            for (PostFacebook postFacebook : readDataFromResponse(json, PostResponse.class).getData()) {
                value.add(createAndFillPostFromPostFacebook(postFacebook));
            }

            result.setValue(new PostsResponse(value));

        } catch (FacebookException e) {
            //TODO: пишем в логер
            result.setStatus(ResponseStatus.ERROR);
            result.getErrors().add(e.getMessage());
        }

        return result;

    }

    @Override
    public ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth) {

        ResponseObject<PostEntity> result = new ResponseObject<PostEntity>();

        HttpGet query = new HttpGet(fqlFindLastPost + accessTokenParamName + getAccessToken(auth));

        try {

            String json = client.processRequest(query).getBody();

            checkErrorInResponse(json);

            PostFacebook postFacebook = readDataFromResponse(json, PostFacebook.class);
            result.setValue(createAndFillPostFromPostFacebook(postFacebook));

        } catch (FacebookException e) {
            //TODO: пишем в логер
            LOGGER.error(e.getMessage(), e);
            result.setStatus(ResponseStatus.ERROR);
            result.getErrors().add(e.getMessage());
        }

        return result;
    }

    private String writePostToWall(SocialAuthEntity auth, PostEntity postEntity) throws FacebookException {
        String url = graphWritePost + StringEscapeUtils.unescapeHtml4(postEntity.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth);
        return client.processRequest(new HttpPost(url)).getBody();
    }

    private void checkErrorInResponse(String json) throws FacebookException {
        if (json.contains("error")) {
            throw new FacebookException("Facebook response contains error: " + json);
        }
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

    private <T> T readDataFromResponse(String json, Class<T> clazz) throws FacebookException {

        T result = null;
        if (!Strings.isNullOrEmpty(json)) {
            try {
                result = objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                throw new FacebookException(e.getMessage());
            }

        }
        return result;
    }

}
