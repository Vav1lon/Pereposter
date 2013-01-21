package com.pereposter.social.api.vkontakte;

import com.pereposter.social.api.SocialService;
import com.pereposter.social.api.entity.*;

import javax.jws.WebService;

@WebService
public interface SocialVkontakteServices extends SocialService {

    String findLastPost(FindPostRequest request);

    PostEntity getLastPost(String requestId);

    String findPostById(FindPostRequest request);

    PostEntity getPostById(String requestId);

    String findPostsByOverCreateDate(FindPostRequest request);

    PostsResponse getPostsByOverCreateDate(String requestId);

    String writePost(WritePostRequest request);

    PostEntity getWritePost(String requestId);

    String writePosts(WritePostsRequest request);

    PostEntity getWritePosts(String requestId);

}
