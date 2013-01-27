package com.pereposter.social.api.entity;

import org.joda.time.DateTime;

import java.io.Serializable;

public class FindPostRequest implements Serializable {

    private SocialAuthEntity socialAuthEntity;
    private String postId;
    private DateTime createdDate;

    public FindPostRequest() {
    }

    public FindPostRequest(SocialAuthEntity socialAuthEntity, String postId) {
        this.socialAuthEntity = socialAuthEntity;
        this.postId = postId;
    }

    public FindPostRequest(SocialAuthEntity socialAuthEntity, DateTime createdDate) {
        this.socialAuthEntity = socialAuthEntity;
        this.createdDate = createdDate;
    }

    public FindPostRequest(SocialAuthEntity socialAuthEntity) {
        this.socialAuthEntity = socialAuthEntity;
    }

    public SocialAuthEntity getSocialAuthEntity() {
        return socialAuthEntity;
    }

    public void setSocialAuthEntity(SocialAuthEntity socialAuthEntity) {
        this.socialAuthEntity = socialAuthEntity;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
