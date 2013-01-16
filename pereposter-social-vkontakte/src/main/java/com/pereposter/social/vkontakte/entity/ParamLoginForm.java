package com.pereposter.social.vkontakte.entity;

public class ParamLoginForm {

    private String ipHParam;
    private String toParam;
    private String actionUrlParam;

    public ParamLoginForm() {
    }

    public ParamLoginForm(String ipHParam, String toParam, String actionUrlParam) {
        this.ipHParam = ipHParam;
        this.toParam = toParam;
        this.actionUrlParam = actionUrlParam;
    }

    public String getIpHParam() {
        return ipHParam;
    }

    public void setIpHParam(String ipHParam) {
        this.ipHParam = ipHParam;
    }

    public String getToParam() {
        return toParam;
    }

    public void setToParam(String toParam) {
        this.toParam = toParam;
    }

    public String getActionUrlParam() {
        return actionUrlParam;
    }

    public void setActionUrlParam(String actionUrlParam) {
        this.actionUrlParam = actionUrlParam;
    }
}
