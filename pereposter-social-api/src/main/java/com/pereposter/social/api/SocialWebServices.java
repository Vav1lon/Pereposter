package com.pereposter.social.api;

import com.pereposter.social.api.entity.*;

import javax.jws.WebService;

@WebService
public interface SocialWebServices extends SocialService {

    String findLastPost(FindPostRequest request);

    ResponseObject<PostEntity> getLastPost(String requestId);

    String findPostById(FindPostRequest request);

    ResponseObject<PostEntity> getPostById(String requestId);

    String findPostsByOverCreateDate(FindPostRequest request);

    ResponseObject<PostsResponse> getPostsByOverCreateDate(String requestId);

    String writePost(WritePostRequest request);

    ResponseObject<String> getWritePost(String requestId);

    String writePosts(WritePostsRequest request);

    ResponseObject<String> getWritePosts(String requestId);

}
