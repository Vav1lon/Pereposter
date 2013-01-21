package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.SocialAuthService;
import com.pereposter.social.vkontakte.entity.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
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
    private AccessTokenService accessTokenService;

    @PostConstruct
    private void initConnector() {
        accessTokenMap = new ConcurrentHashMap<String, AccessToken>();
    }

    //TODO: проверитьм етод!!
    public synchronized void checkValidToken() {

        long currentTimeStamp = new DateTime().getMillis();

        for (Map.Entry<String, AccessToken> entry : accessTokenMap.entrySet()) {

            if (currentTimeStamp > (entry.getValue().getExpiresIn() * 1000)) {
                accessTokenMap.remove(entry);
            }

        }

    }

    @Override
    public String writeNewPost(SocialAuthService auth, PostEntity postEntity) {
        String result = writePostToWall(auth, postEntity);
        return Strings.isNullOrEmpty(result) ? null : result;
    }

    @Override
    public String writeNewPosts(SocialAuthService auth, List<PostEntity> postEntities) {

        WritePostResponse response = null;
        HttpPost httpPost;

        for (PostEntity postEntity : postEntities) {

            httpPost = new HttpPost(writePostToUserWallUrl + StringEscapeUtils.escapeHtml4(postEntity.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth));

            String json = client.sendRequestReturnBodyAndResponse(httpPost, false).getBody();
            response = readJsonToObject(json, WritePostResponse.class);
        }

        String result = null;
        if (response != null && !Strings.isNullOrEmpty(response.getResponse().getPost_id()))
            result = response.getResponse().getPost_id();

        return result;

    }

    @Override
    public PostEntity findPostById(SocialAuthService auth, String postId) {


        HttpGet httpPost = new HttpGet(getPostByIdUrl + postId + "_" + auth.getUserId() + accessTokenParamName + getAccessToken(auth));

        String json = client.sendRequestReturnBodyAndResponse(httpPost, false).getBody();

        FindPostByIdResponse response = readJsonToObject(json, FindPostByIdResponse.class);

        PostEntity result = null;

        if (response != null && response.getResponse().size() != 0) {
            result = createPost(response.getResponse().get(0));
        }

        return result;

    }

    @Override
    public List<PostEntity> findPostsByOverCreatedDate(SocialAuthService auth, DateTime createdDate) {

        //TODO: fix logic!!!!

        Integer count = 50;
        Integer offset = 0;

        List<PostEntity> result = findPostOverCreateDate(auth, createdDate, count, offset);

        return result;
    }

    @Override
    public PostEntity findLastPost(SocialAuthService auth) {

        HttpGet httpGet = new HttpGet(getPostsFormWall + "?filter=owner&count=" + 1 + accessTokenParamName + getAccessToken(auth));
        String json = client.sendRequestReturnBodyAndResponse(httpGet, false).getBody();
        GetPostListResponse response = readJsonToObject(json, GetPostListResponse.class);

        PostEntity result = null;

        if (response != null && response.getResponse().size() == 2) {

            PostVkontakte postVkontakte = createAndFillPostVkontakte((Map<String, Object>) response.getResponse().get(1));

            result = createPost(postVkontakte);
        }

        return result;
    }

    private String getAccessToken(SocialAuthService auth) {
        AccessToken result;
        result = accessTokenMap.get(auth.getUserId());

        if (result == null) {
            result = accessTokenService.getNewAccessToken(auth);
            accessTokenMap.put(result.getUserId(), result);
        }
        return result.getAccessToken();
    }

    private PostVkontakte createAndFillPostVkontakte(Map<String, Object> map) {

        PostVkontakte result = new PostVkontakte();

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getKey().equals("id")) {
                result.setId(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("from_id")) {
                result.setDate(Long.parseLong(entry.getValue().toString()));
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

    private List<PostEntity> findPostOverCreateDate(SocialAuthService auth, DateTime createdDate, Integer count, Integer offset) {
        HttpGet httpGet = new HttpGet(getPostsFormWall + "?filter=owner&count=" + count + "&offset=" + offset + accessTokenParamName + getAccessToken(auth));

        String json = client.sendRequestReturnBodyAndResponse(httpGet, true).getBody();

        GetPostListResponse response = readJsonToObject(json, GetPostListResponse.class);

        List<PostEntity> result = new ArrayList<PostEntity>();
        PostVkontakte postVkontakte;

        for (int i = 1; i < response.getResponse().size(); i++) {

            postVkontakte = createAndFillPostVkontakte((Map<String, Object>) response.getResponse().get(i));

            if (postVkontakte.getDate() > (createdDate.getMillis() / 1000)) {
                result.add(createPost(postVkontakte));
            } else {
                break;
            }

        }


        return result.isEmpty() ? null : result;
    }

    private String writePostToWall(SocialAuthService auth, PostEntity postEntity) {
        HttpPost httpPost = new HttpPost(writePostToUserWallUrl + StringEscapeUtils.escapeHtml4(postEntity.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth));

        String json = client.sendRequestReturnBodyAndResponse(httpPost, false).getBody();
        WritePostResponse response = readJsonToObject(json, WritePostResponse.class);

        return response.getResponse().getPost_id();
    }

    private PostEntity createPost(PostVkontakte postVkontakte) {
        PostEntity result = new PostEntity();
        result.setCreatedDate(new DateTime(postVkontakte.getDate() * 1000));
        result.setId(postVkontakte.getId().toString());
        result.setMessage(postVkontakte.getText());
        return result;
    }

    private PostEntity createPost(FindPostByIdResult findPostResponse) {
        PostEntity result = new PostEntity();
        result.setId(findPostResponse.getId().toString());
        result.setMessage(findPostResponse.getText());
        result.setCreatedDate(new DateTime(findPostResponse.getDate() * 1000));
        return result;
    }

    private <T> T readJsonToObject(String json, Class<T> clazz) {

        T result = null;
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            //TODO: пишем ошибку в лог
        }

        return result;

    }

}
