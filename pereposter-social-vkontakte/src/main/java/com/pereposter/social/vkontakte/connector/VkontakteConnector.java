package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.api.social.VkontakteException;
import com.pereposter.social.vkontakte.entity.*;
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

@Component
public class VkontakteConnector implements SocialNetworkConnector {

    private final static Logger LOGGER = LoggerFactory.getLogger(VkontakteConnector.class);

    @Value("${pereposter.social.vkontakte.url.write.post.wall}")
    private String writePostToUserWallUrl;

    @Value("${pereposter.social.vkontakte.url.get.postById}")
    private String getPostByIdUrl;

    @Value("${pereposter.social.vkontakte.url.get.postList}")
    private String getPostsFormWall;

    @Value("${pereposter.social.vkontakte.url.accessTokenParamName}")
    private String accessTokenParamName;

    private Map<String, AccessToken> accessTokenMap = null;

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccessTokenService vkontakteAccessTokenService;

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

    @Override
    public ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {

        ResponseObject<String> result = new ResponseObject<String>();

        if (!Strings.isNullOrEmpty(postEntity.getMessage())) {

            try {
                result.setValue(writePostToWall(auth, postEntity));
            } catch (VkontakteException e) {
                //TODO: write to log
                result.setStatus(ResponseStatus.ERROR);
                result.getErrors().add(e.getMessage());
            }
        }

        return result;
    }

    @Override
    public ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {

        ResponseObject<String> result = new ResponseObject<String>();

        for (PostEntity postEntity : postEntities) {
            if (!Strings.isNullOrEmpty(postEntity.getMessage())) {
                try {
                    result.setValue(writePostToWall(auth, postEntity));
                } catch (VkontakteException e) {
                    result.setStatus(ResponseStatus.WARNING);
                    result.getErrors().add(e.getMessage());
                }
            }

        }

        if (result.getErrors().size() == postEntities.size()) {
            //TODO: писать ошибку в лог
            result.setStatus(ResponseStatus.ERROR);
        }

        return result;
    }

    @Override
    public ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId) {

        ResponseObject<PostEntity> result = new ResponseObject<PostEntity>();

        FindPostByIdResponse response = null;
        String url = getPostByIdUrl + postId + "_" + auth.getUserId() + accessTokenParamName + getAccessToken(auth);

        try {

            String json = client.processRequest(new HttpGet(url), false).getBody();

            if (!json.contains("error")) {
                response = readJsonToObject(json, FindPostByIdResponse.class);
            } else {
                //TODO: писать ошибку в лог
                throw new VkontakteException("Error, request writeNewPosts throw: " + json);
            }

        } catch (VkontakteException e) {
            LOGGER.error(e.getMessage());
            result.setStatus(ResponseStatus.ERROR);
            result.getErrors().add(e.getMessage());
        }


        if (result.getStatus() != ResponseStatus.ERROR) {

            if (response != null && response.getResponse().size() != 0) {
                result.setValue(createPost(response.getResponse().get(0)));
            }
        }

        return result;

    }

    @Override
    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {

        //TODO: fix logic!!!!

        Integer count = 50;
        Integer offset = 0;

        return findPostOverCreateDate(auth, createdDate, count, offset);
    }

    @Override
    public ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth) {

        ResponseObject<PostEntity> result = new ResponseObject<PostEntity>();
        GetPostListResponse response = null;

        String url = getPostsFormWall + "?filter=owner&count=1" + accessTokenParamName + getAccessToken(auth);

        try {
            String json = client.processRequest(new HttpGet(url), false).getBody();

            if (!json.contains("error")) {
                response = readJsonToObject(json, GetPostListResponse.class);
            } else {
                //TODO: писать ошибку в лог
                throw new VkontakteException("Error, request findLostPost throw: " + json);
            }
        } catch (VkontakteException e) {
            LOGGER.error(e.getMessage());
            result.setStatus(ResponseStatus.ERROR);
            result.getErrors().add(e.getMessage());
        }

        if (result.getStatus() != ResponseStatus.ERROR) {

            if (response != null && response.getResponse().size() == 2) {

                PostVkontakte postVkontakte = createAndFillPostVkontakte((Map<String, Object>) response.getResponse().get(1));

                result.setValue(createPost(postVkontakte));
            }
        }

        return result;
    }

    private synchronized AccessToken getValueAccessTokenMap(String key) {
        return accessTokenMap.get(key);
    }

    private String getAccessToken(SocialAuthEntity auth) {

        AccessToken result = null;
        if (auth.getUserId() != null) {
            result = getValueAccessTokenMap(auth.getUserId());
        }

        try {
            if (result == null) {
                result = vkontakteAccessTokenService.getNewAccessToken(auth);
                accessTokenMap.put(result.getUserId(), result);
            }
        } catch (Exception e) {
            // TODO: write log
            LOGGER.error("can not access to use for SocialAuthEntity: " + auth.toString());
        }

        return result != null ? result.getAccessToken() : null;
    }

    private PostVkontakte createAndFillPostVkontakte(Map<String, Object> map) {

        PostVkontakte result = new PostVkontakte();

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getKey().equals("id")) {
                result.setId(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("from_id")) {
                result.setFrom_id(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("to_id")) {
                result.setTo_id(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("date")) {
                result.setDate(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("text")) {
                result.setText((String) entry.getValue());
            }


        }

        return result;

    }

    private ResponseObject<PostsResponse> findPostOverCreateDate(SocialAuthEntity auth, DateTime createdDate, Integer count, Integer offset) {

        ResponseObject<PostsResponse> result = new ResponseObject<PostsResponse>();
        GetPostListResponse response = null;

        HttpGet httpGet = new HttpGet(getPostsFormWall + "?filter=owner&count=" + count + "&offset=" + offset + accessTokenParamName + getAccessToken(auth));

        try {
            String json = client.processRequest(httpGet, true).getBody();

            if (!json.contains("error")) {
                response = readJsonToObject(json, GetPostListResponse.class);
            } else {
                throw new VkontakteException("Error, request findPostOverCreateDate throw: " + json);
            }

            List<PostEntity> values = new ArrayList<PostEntity>();
            PostVkontakte postVkontakte;

            for (int i = 1; i < response.getResponse().size(); i++) {

                postVkontakte = createAndFillPostVkontakte((Map<String, Object>) response.getResponse().get(i));

                if (postVkontakte.getDate() > (createdDate.getMillis() / 1000)) {
                    values.add(createPost(postVkontakte));
                } else {
                    break;
                }

            }

            result.setValue(new PostsResponse(values));

        } catch (VkontakteException e) {
            //TODO: писать ошибку в лог
            LOGGER.error(e.getMessage());
            result.setStatus(ResponseStatus.ERROR);
            result.getErrors().add(e.getMessage());
        }

        return result;
    }

    private String writePostToWall(SocialAuthEntity auth, PostEntity postEntity) throws VkontakteException {

        HttpPost httpPost = new HttpPost(writePostToUserWallUrl + StringEscapeUtils.escapeHtml4(postEntity.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth));

        String json = client.processRequest(httpPost, false).getBody();

        if (json.contains("error")) {
            throw new VkontakteException("Error: request writePostToWall throw: " + json);
        }

        WritePostResponse response = readJsonToObject(json, WritePostResponse.class);

        return response.getResponse().getPost_id();
    }

    private PostEntity createPost(PostVkontakte postVkontakte) {
        PostEntity result = new PostEntity();
        result.setCreatedDate(new DateTime(postVkontakte.getDate() * 1000));
        result.setId(postVkontakte.getId().toString());
        result.setMessage(postVkontakte.getText());
        result.setOwnerId(postVkontakte.getFrom_id().toString());
        return result;
    }

    private PostEntity createPost(FindPostByIdResult findPostResponse) {
        PostEntity result = new PostEntity();
        result.setId(findPostResponse.getId().toString());
        result.setMessage(findPostResponse.getText());
        result.setCreatedDate(new DateTime(findPostResponse.getDate() * 1000));
        return result;
    }

    private <T> T readJsonToObject(String json, Class<T> clazz) throws VkontakteException {

        T result = null;
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new VkontakteException("json parse error: " + e.getMessage(), e);
        }

        return result;

    }

}
