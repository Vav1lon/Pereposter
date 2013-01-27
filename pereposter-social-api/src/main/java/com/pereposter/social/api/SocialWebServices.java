package com.pereposter.social.api;

import com.pereposter.social.api.entity.*;

import javax.jws.WebService;

@WebService
public interface SocialWebServices extends SocialService {

    String findLastPost(FindPostRequest request);

    PostEntity getLastPost(String requestId);

    String findPostById(FindPostRequest request);

    PostEntity getPostById(String requestId);

    String findPostsByOverCreateDate(FindPostRequest request);

    PostsResponse getPostsByOverCreateDate(String requestId);

    String writePost(WritePostRequest request);

    String getWritePost(String requestId);

    String writePosts(WritePostsRequest request);

    String getWritePosts(String requestId);

}
