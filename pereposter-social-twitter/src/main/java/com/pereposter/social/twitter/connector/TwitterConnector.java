package com.pereposter.social.twitter.connector;

import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import com.pereposter.social.api.entity.SocialAuthEntity;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TwitterConnector implements SocialNetworkConnector {

    @Autowired
    private Client client;

    @Autowired
    private AccessTokenService accessTokenService;

    @Override
    public ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {
        return null;
    }

    @Override
    public ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {
        return null;
    }

    @Override
    public ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId) {
        return null;
    }

    @Override
    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {
        return null;
    }

    @Override
    public ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth) {

        accessTokenService.getAccessToken();

        return null;
    }
}
