package com.pereposter.social.api;

public class TwitterException extends Exception {

    public TwitterException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TwitterException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TwitterException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public TwitterException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected TwitterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
