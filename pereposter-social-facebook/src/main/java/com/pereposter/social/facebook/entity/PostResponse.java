package com.pereposter.social.facebook.entity;

import java.util.List;

public class PostResponse {

    private List<PostFacebook> data;

    public PostResponse() {
    }

    public PostResponse(List<PostFacebook> data) {
        this.data = data;
    }

    public List<PostFacebook> getData() {
        return data;
    }

    public void setData(List<PostFacebook> data) {
        this.data = data;
    }
}
