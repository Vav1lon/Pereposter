package com.pereposter.social.api.entity;

import java.io.Serializable;
import java.util.List;

public class WritePostsRequest implements Serializable {

    private SocialAuthEntity socialAuthEntity;
    private List<PostEntity> postsService;

    public WritePostsRequest() {
    }

    public WritePostsRequest(SocialAuthEntity socialAuthEntity, List<PostEntity> postsService) {
        this.socialAuthEntity = socialAuthEntity;
        this.postsService = postsService;
    }

    public SocialAuthEntity getSocialAuthEntity() {
        return socialAuthEntity;
    }

    public void setSocialAuthEntity(SocialAuthEntity socialAuthEntity) {
        this.socialAuthEntity = socialAuthEntity;
    }

    public List<PostEntity> getPostsService() {
        return postsService;
    }

    public void setPostsService(List<PostEntity> postsService) {
        this.postsService = postsService;
    }
}
