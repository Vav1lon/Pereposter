package com.pereposter.social.facebook;

import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;
import com.pereposter.social.facebook.connector.FacebookConnector;

public class FacebookConnectorWrapper {

    private SocialNetworkConnector facebookConnector;

    public FacebookConnectorWrapper(FacebookConnector facebookConnector) {
        this.facebookConnector = facebookConnector;
    }

    public FacebookConnectorWrapper(SocialNetworkConnector facebookConnector) {
        this.facebookConnector = facebookConnector;
    }

    public ResponseObject<String> writeNewPost(WritePostRequest writePostRequest) {
        return facebookConnector.writeNewPost(writePostRequest.getSocialAuthEntity(), writePostRequest.getPostEntity());
    }

    public ResponseObject<String> writeNewPosts(WritePostsRequest writePostsRequest) {
        return facebookConnector.writeNewPosts(writePostsRequest.getSocialAuthEntity(), writePostsRequest.getPostsService());
    }

    public ResponseObject<PostEntity> findPostById(FindPostRequest findPostRequest) {
        return facebookConnector.findPostById(findPostRequest.getSocialAuthEntity(), findPostRequest.getPostId());
    }

    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(FindPostRequest findPostRequest) {
        return facebookConnector.findPostsByOverCreatedDate(findPostRequest.getSocialAuthEntity(), findPostRequest.getCreatedDate());
    }

    public ResponseObject<PostEntity> findLastPost(FindPostRequest findPostRequest) {
        return facebookConnector.findLastPost(findPostRequest.getSocialAuthEntity());
    }
}
