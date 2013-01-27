package com.pereposter.social.api.entity;

import java.io.Serializable;

public class WritePostRequest implements Serializable {

    private SocialAuthEntity socialAuthEntity;
    private PostEntity postEntity;

    public WritePostRequest() {
    }

    public WritePostRequest(SocialAuthEntity socialAuthEntity, PostEntity postEntity) {
        this.socialAuthEntity = socialAuthEntity;
        this.postEntity = postEntity;
    }

    public SocialAuthEntity getSocialAuthEntity() {
        return socialAuthEntity;
    }

    public void setSocialAuthEntity(SocialAuthEntity socialAuthEntity) {
        this.socialAuthEntity = socialAuthEntity;
    }

    public PostEntity getPostEntity() {
        return postEntity;
    }

    public void setPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }
}
