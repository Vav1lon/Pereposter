package com.pereposter.entity.internal;

public enum SocialNetworkEnum {

    FACEBOOK(1, "facebookControl"),
    VKONTAKTE(2, "vkontakteControl"),
    TWITTER(3, "twitterControl");

    private int id;
    private String serviceName;

    SocialNetworkEnum(int id, String serviceName) {
        this.id = id;
        this.serviceName = serviceName;
    }

    public int getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static SocialNetworkEnum fromInt(int id) {
        for (SocialNetworkEnum type : SocialNetworkEnum.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("SocialNetworkEnum not found by id : " + id);
    }

}
