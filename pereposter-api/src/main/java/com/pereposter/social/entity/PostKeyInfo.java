package com.pereposter.social.entity;

import org.joda.time.DateTime;

public class PostKeyInfo {

    public PostKeyInfo() {
    }

    public PostKeyInfo(String id, DateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    private String id;
    private DateTime createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
