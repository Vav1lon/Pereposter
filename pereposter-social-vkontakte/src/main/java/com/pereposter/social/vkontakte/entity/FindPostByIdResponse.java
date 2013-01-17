package com.pereposter.social.vkontakte.entity;

import java.util.List;

public class FindPostByIdResponse {

    private List<FindPostByIdResult> response;

    public List<FindPostByIdResult> getResponse() {
        return response;
    }

    public void setResponse(List<FindPostByIdResult> response) {
        this.response = response;
    }
}
