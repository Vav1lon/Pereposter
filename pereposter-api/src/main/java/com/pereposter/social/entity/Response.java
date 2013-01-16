package com.pereposter.social.entity;

import org.apache.http.HttpResponse;

public class Response {

    private String body;
    private HttpResponse httpResponse;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }
}
