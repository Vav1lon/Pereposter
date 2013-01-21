package com.pereposter.social.api.entity;

import java.io.Serializable;

public class WritePostRequest implements Serializable {

    private SocialAuthService socialAuthService;
    private PostEntity postEntity;

    public WritePostRequest() {
    }

    public WritePostRequest(SocialAuthService socialAuthService, PostEntity postEntity) {
        this.socialAuthService = socialAuthService;
        this.postEntity = postEntity;
    }

    public SocialAuthService getSocialAuthService() {
        return socialAuthService;
    }

    public void setSocialAuthService(SocialAuthService socialAuthService) {
        this.socialAuthService = socialAuthService;
    }

    public PostEntity getPostEntity() {
        return postEntity;
    }

    public void setPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }
}
