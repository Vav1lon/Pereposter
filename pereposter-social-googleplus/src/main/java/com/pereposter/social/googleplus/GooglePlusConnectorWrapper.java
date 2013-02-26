package com.pereposter.social.googleplus;

import com.pereposter.social.api.ConnectorWrapper;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;

public class GooglePlusConnectorWrapper extends ConnectorWrapper {

    private SocialNetworkConnector vkontakteConnector;

    public GooglePlusConnectorWrapper(SocialNetworkConnector vkontakteConnector) {
        this.vkontakteConnector = vkontakteConnector;
    }

    public ResponseObject<String> writeNewPost(WritePostRequest writePostRequest) {
        return vkontakteConnector.writeNewPost(writePostRequest.getSocialAuthEntity(), writePostRequest.getPostEntity());
    }

    public ResponseObject<String> writeNewPosts(WritePostsRequest writePostsRequest) {
        return vkontakteConnector.writeNewPosts(writePostsRequest.getSocialAuthEntity(), writePostsRequest.getPostsService());
    }

    public ResponseObject<PostEntity> findPostById(FindPostRequest findPostRequest) {
        return vkontakteConnector.findPostById(findPostRequest.getSocialAuthEntity(), findPostRequest.getPostId());
    }

    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(FindPostRequest findPostRequest) {
        return vkontakteConnector.findPostsByOverCreatedDate(findPostRequest.getSocialAuthEntity(), findPostRequest.getCreatedDate());
    }

    public ResponseObject<PostEntity> findLastPost(FindPostRequest findPostRequest) {
        return vkontakteConnector.findLastPost(findPostRequest.getSocialAuthEntity());
    }

}
