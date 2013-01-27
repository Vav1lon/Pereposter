package com.pereposter.social.vkontakte;

import com.pereposter.social.api.ConnectorWrapper;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;

public class VkontakteConnectorWrapper extends ConnectorWrapper {

    private SocialNetworkConnector vkontakteConnector;

    public VkontakteConnectorWrapper(SocialNetworkConnector vkontakteConnector) {
        this.vkontakteConnector = vkontakteConnector;
    }

    public String writeNewPost(WritePostRequest writePostRequest) {
        return vkontakteConnector.writeNewPost(writePostRequest.getSocialAuthEntity(), writePostRequest.getPostEntity());
    }

    public String writeNewPosts(WritePostsRequest writePostsRequest) {
        return vkontakteConnector.writeNewPosts(writePostsRequest.getSocialAuthEntity(), writePostsRequest.getPostsService());
    }

    public PostEntity findPostById(FindPostRequest findPostRequest) {
        return vkontakteConnector.findPostById(findPostRequest.getSocialAuthEntity(), findPostRequest.getPostId());
    }

    public PostsResponse findPostsByOverCreatedDate(FindPostRequest findPostRequest) {
        return new PostsResponse(vkontakteConnector.findPostsByOverCreatedDate(findPostRequest.getSocialAuthEntity(), findPostRequest.getCreatedDate()));
    }

    public PostEntity findLastPost(FindPostRequest findPostRequest) {
        return vkontakteConnector.findLastPost(findPostRequest.getSocialAuthEntity());
    }

}
