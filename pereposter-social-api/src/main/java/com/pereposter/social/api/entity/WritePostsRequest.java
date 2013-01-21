package com.pereposter.social.api.entity;

import java.io.Serializable;
import java.util.List;

public class WritePostsRequest implements Serializable {

    private SocialAuthService socialAuthService;
    private List<PostEntity> postsService;

    public WritePostsRequest() {
    }

    public WritePostsRequest(SocialAuthService socialAuthService, List<PostEntity> postsService) {
        this.socialAuthService = socialAuthService;
        this.postsService = postsService;
    }

    public SocialAuthService getSocialAuthService() {
        return socialAuthService;
    }

    public void setSocialAuthService(SocialAuthService socialAuthService) {
        this.socialAuthService = socialAuthService;
    }

    public List<PostEntity> getPostsService() {
        return postsService;
    }

    public void setPostsService(List<PostEntity> postsService) {
        this.postsService = postsService;
    }
}
