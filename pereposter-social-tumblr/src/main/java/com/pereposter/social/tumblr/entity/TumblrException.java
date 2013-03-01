package com.pereposter.social.tumblr.entity;

public class TumblrException extends Exception {

    public TumblrException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TumblrException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TumblrException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TumblrException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected TumblrException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
