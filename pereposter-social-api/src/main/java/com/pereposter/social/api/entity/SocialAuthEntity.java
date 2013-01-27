package com.pereposter.social.api.entity;

import java.io.Serializable;

public class SocialAuthEntity implements Serializable {

    private String login;
    private String password;
    private String userId;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
