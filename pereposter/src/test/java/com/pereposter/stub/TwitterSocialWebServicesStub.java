package com.pereposter.stub;

import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import org.springframework.stereotype.Component;

@Component("twitterSocialService")
public class TwitterSocialWebServicesStub implements SocialWebServices {

    @Override
    public String findLastPost(FindPostRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostEntity> getLastPost(String requestId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String findPostById(FindPostRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostEntity> getPostById(String requestId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String findPostsByOverCreateDate(FindPostRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<PostsResponse> getPostsByOverCreateDate(String requestId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String writePost(WritePostRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<String> getWritePost(String requestId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String writePosts(WritePostsRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResponseObject<String> getWritePosts(String requestId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RequestStatus getStatus(String requestId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void cancelRequest(String requestId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
