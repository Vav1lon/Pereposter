package com.pereposter.social.api.social;

public class VkontakteException extends Exception {

    public VkontakteException() {
        super();
    }

    public VkontakteException(String message) {
        super(message);
    }

    public VkontakteException(String message, Throwable cause) {
        super(message, cause);
    }

    public VkontakteException(Throwable cause) {
        super(cause);
    }

    protected VkontakteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
