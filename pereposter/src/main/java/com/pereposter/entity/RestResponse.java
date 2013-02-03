package com.pereposter.entity;

public class RestResponse {

    public RestResponse() {
    }

    public RestResponse(String error) {
        this.error = error;
    }

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
