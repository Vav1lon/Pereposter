package com.pereposter.social.api.entity;

import org.joda.time.DateTime;

import java.io.Serializable;

public class FindPostRequest implements Serializable {

    private SocialAuthService socialAuthService;
    private String postId;
    private DateTime createdDate;

    public FindPostRequest() {
    }

    public FindPostRequest(SocialAuthService socialAuthService, String postId) {
        this.socialAuthService = socialAuthService;
        this.postId = postId;
    }

    public FindPostRequest(SocialAuthService socialAuthService, DateTime createdDate) {
        this.socialAuthService = socialAuthService;
        this.createdDate = createdDate;
    }

    public FindPostRequest(SocialAuthService socialAuthService) {
        this.socialAuthService = socialAuthService;
    }

    public SocialAuthService getSocialAuthService() {
        return socialAuthService;
    }

    public void setSocialAuthService(SocialAuthService socialAuthService) {
        this.socialAuthService = socialAuthService;
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
