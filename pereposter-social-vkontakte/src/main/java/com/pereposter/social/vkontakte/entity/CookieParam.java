package com.pereposter.social.vkontakte.entity;

public class CookieParam {

    private String loginParam;
    private String passParam;

    public CookieParam() {
    }

    public CookieParam(String loginParam, String passParam) {
        this.loginParam = loginParam;
        this.passParam = passParam;
    }

    public String getLoginParam() {
        return loginParam;
    }

    public void setLoginParam(String loginParam) {
        this.loginParam = loginParam;
    }

    public String getPassParam() {
        return passParam;
    }

    public void setPassParam(String passParam) {
        this.passParam = passParam;
    }
}
