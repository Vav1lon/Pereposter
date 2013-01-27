package com.pereposter.social.api.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseObject<T> implements Serializable {

    private List<String> errors = new ArrayList<String>();
    private ResponseStatus status = ResponseStatus.OK;
    private T value;

    public List<String> getErrors() {
        return errors;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
