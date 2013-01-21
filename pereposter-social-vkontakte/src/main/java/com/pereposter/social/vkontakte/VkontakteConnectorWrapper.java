package com.pereposter.social.vkontakte;

import com.pereposter.social.api.ConnectorWrapper;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;

public class VkontakteConnectorWrapper extends ConnectorWrapper {

    private SocialNetworkConnector vkontakteConnector;

    public VkontakteConnectorWrapper(SocialNetworkConnector vkontakteConnector) {
        this.vkontakteConnector = vkontakteConnector;
    }

    public PostEntity writeNewPost(WritePostRequest writePostRequest) {
        String postId = vkontakteConnector.writeNewPost(writePostRequest.getSocialAuthService(), writePostRequest.getPostEntity());
        return vkontakteConnector.findPostById(writePostRequest.getSocialAuthService(), postId);
    }

    public PostEntity writeNewPosts(WritePostsRequest writePostsRequest) {
        String postId = vkontakteConnector.writeNewPosts(writePostsRequest.getSocialAuthService(), writePostsRequest.getPostsService());
        return vkontakteConnector.findPostById(writePostsRequest.getSocialAuthService(), postId);
    }

    public PostEntity findPostById(FindPostRequest findPostRequest) {
        return vkontakteConnector.findPostById(findPostRequest.getSocialAuthService(), findPostRequest.getPostId());
    }

    public PostsResponse findPostsByOverCreatedDate(FindPostRequest findPostRequest) {
        return new PostsResponse(vkontakteConnector.findPostsByOverCreatedDate(findPostRequest.getSocialAuthService(), findPostRequest.getCreatedDate()));
    }

    public PostEntity findLastPost(FindPostRequest findPostRequest) {
        return vkontakteConnector.findLastPost(findPostRequest.getSocialAuthService());
    }

}
