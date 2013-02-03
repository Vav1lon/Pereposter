package com.pereposter.stub;

import com.pereposter.entity.Post;
import com.pereposter.social.api.SocialWebServices;
import com.pereposter.social.api.entity.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component("vkontakteSocialService")
public class VkontakteSocialWebServicesStub implements SocialWebServices {

    private List<Post> vkontaktePosts;

    private Map<String, Object> requestMap;


    @PostConstruct
    private void setUp() {
        vkontaktePosts = new ArrayList<Post>();
        requestMap = new HashMap<String, Object>();
    }

    @Override
    public String findLastPost(FindPostRequest request) {

        String uuid = UUID.randomUUID().toString();

        requestMap.put(uuid, new Object());

        return uuid;
    }

    @Override
    public ResponseObject<PostEntity> getLastPost(String requestId) {
        Object obj = requestMap.get(requestId);

        ResponseObject<PostEntity> aa = new ResponseObject<PostEntity>();

        PostEntity postEntity = new PostEntity();

//        postEntity.



        return new ResponseObject<PostEntity>();
    }

    @Override
    public String findPostById(FindPostRequest request) {
        return null;
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
        return requestMap.containsKey(requestId) ? RequestStatus.READY : RequestStatus.NOT_FOUND;
    }

    @Override
    public void cancelRequest(String requestId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<Post> getVkontaktePosts() {
        return vkontaktePosts;
    }

}
