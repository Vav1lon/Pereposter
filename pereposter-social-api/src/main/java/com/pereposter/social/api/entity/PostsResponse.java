package com.pereposter.social.api.entity;

import java.io.Serializable;
import java.util.List;

public class PostsResponse implements Serializable {

    private List<PostEntity> postEntityList;

    public PostsResponse() {
    }

    public PostsResponse(List<PostEntity> postEntityList) {
        this.postEntityList = postEntityList;
    }

    public List<PostEntity> getPostEntityList() {
        return postEntityList;
    }

    public void setPostEntityList(List<PostEntity> postEntityList) {
        this.postEntityList = postEntityList;
    }
}
