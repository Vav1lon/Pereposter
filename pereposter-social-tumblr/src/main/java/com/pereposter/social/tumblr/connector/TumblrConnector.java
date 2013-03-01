package com.pereposter.social.tumblr.connector;

import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.tumblr.entity.AccessToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TumblrConnector implements SocialNetworkConnector {

    private final static Logger LOGGER = LoggerFactory.getLogger(TumblrConnector.class);

    @Value("${pereposter.social.tumblr.url.write.post.wall}")
    private String writePostToUserWallUrl;

    @Value("${pereposter.social.tumblr.url.get.postById}")
    private String getPostByIdUrl;

    @Value("${pereposter.social.tumblr.url.get.postList}")
    private String getPostsFormWall;

    @Value("${pereposter.social.tumblr.url.accessTokenParamName}")
    private String accessTokenParamName;

    private Map<String, AccessToken> accessTokenMap = null;

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccessTokenService tumblrAccessTokenService;

    @PostConstruct
    private void initConnector() {
        accessTokenMap = new ConcurrentHashMap<String, AccessToken>();
    }

    @Override
    public ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth) {
        tumblrAccessTokenService.getAccessToken();
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
