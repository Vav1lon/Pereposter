package com.pereposter.social.googleplus.connector;

import com.pereposter.social.api.GooglePlusException;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import com.pereposter.social.api.entity.SocialAuthEntity;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GooglePlusConnector implements SocialNetworkConnector {

    private final static Logger LOGGER = LoggerFactory.getLogger(GooglePlusConnector.class);

    @Autowired
    private AccessTokenService tokenService;

    @Override
    public ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth) {

        try {
            tokenService.getAccessToken();
        } catch (GooglePlusException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("aaa");

        return null;
    }

    @Override
    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
