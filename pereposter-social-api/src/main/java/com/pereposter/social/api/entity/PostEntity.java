package com.pereposter.social.api.entity;

import org.joda.time.DateTime;

import java.io.Serializable;

public class PostEntity implements Serializable {

    private String id;
    private String message;
    private DateTime createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
