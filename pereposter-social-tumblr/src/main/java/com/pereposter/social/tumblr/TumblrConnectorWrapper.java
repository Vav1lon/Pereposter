package com.pereposter.social.tumblr;

import com.pereposter.social.api.ConnectorWrapper;
import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.entity.*;

public class TumblrConnectorWrapper extends ConnectorWrapper {

    private SocialNetworkConnector tumblrConnector;

    public TumblrConnectorWrapper(SocialNetworkConnector tumblrConnector) {
        this.tumblrConnector = tumblrConnector;
    }

    public ResponseObject<String> writeNewPost(WritePostRequest writePostRequest) {
        return tumblrConnector.writeNewPost(writePostRequest.getSocialAuthEntity(), writePostRequest.getPostEntity());
    }

    public ResponseObject<String> writeNewPosts(WritePostsRequest writePostsRequest) {
        return tumblrConnector.writeNewPosts(writePostsRequest.getSocialAuthEntity(), writePostsRequest.getPostsService());
    }

    public ResponseObject<PostEntity> findPostById(FindPostRequest findPostRequest) {
        return tumblrConnector.findPostById(findPostRequest.getSocialAuthEntity(), findPostRequest.getPostId());
    }

    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(FindPostRequest findPostRequest) {
        return tumblrConnector.findPostsByOverCreatedDate(findPostRequest.getSocialAuthEntity(), findPostRequest.getCreatedDate());
    }

    public ResponseObject<PostEntity> findLastPost(FindPostRequest findPostRequest) {
        return tumblrConnector.findLastPost(findPostRequest.getSocialAuthEntity());
    }

}
