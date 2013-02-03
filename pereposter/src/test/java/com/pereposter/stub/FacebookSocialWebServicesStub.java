package com.pereposter.stub;

import com.pereposter.entity.Post;
import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component("facebookSocialService")
public class FacebookSocialWebServicesStub implements SocialWebServices {

    private List<Post> facebookPosts;

    @PostConstruct
    private void setUp() {
        facebookPosts = new ArrayList<Post>();
    }

    @Override
    public String findLastPost(FindPostRequest request) {
        return null;
    }

    @Override
    public ResponseObject<PostEntity> getLastPost(String requestId) {
        return null;
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

    public List<Post> getFacebookPosts() {
        return facebookPosts;
    }
}
