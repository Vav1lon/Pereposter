package com.pereposter.social.twitter;

import com.pereposter.social.api.ConnectorWrapper;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;

public class TwitterConnectorWrapper extends ConnectorWrapper {

    private SocialNetworkConnector twitterConnector;

    public TwitterConnectorWrapper(SocialNetworkConnector twitterConnector) {
        this.twitterConnector = twitterConnector;
    }

    public ResponseObject<String> writeNewPost(WritePostRequest writePostRequest) {
        return twitterConnector.writeNewPost(writePostRequest.getSocialAuthEntity(), writePostRequest.getPostEntity());
    }

    public ResponseObject<String> writeNewPosts(WritePostsRequest writePostsRequest) {
        return twitterConnector.writeNewPosts(writePostsRequest.getSocialAuthEntity(), writePostsRequest.getPostsService());
    }

    public ResponseObject<PostEntity> findPostById(FindPostRequest findPostRequest) {
        return twitterConnector.findPostById(findPostRequest.getSocialAuthEntity(), findPostRequest.getPostId());
    }

    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(FindPostRequest findPostRequest) {
        return twitterConnector.findPostsByOverCreatedDate(findPostRequest.getSocialAuthEntity(), findPostRequest.getCreatedDate());
    }

    public ResponseObject<PostEntity> findLastPost(FindPostRequest findPostRequest) {
        return twitterConnector.findLastPost(findPostRequest.getSocialAuthEntity());
    }
}