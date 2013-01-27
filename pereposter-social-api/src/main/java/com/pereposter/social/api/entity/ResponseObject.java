package com.pereposter.social.api.entity;

import com.pereposter.social.api.ConnectorErrors;

import java.io.Serializable;
import java.util.List;

public class ResponseObject<T extends Serializable> {

    private List<ConnectorErrors> errors;
    private List<String> errorNotes;
    private ResponseStatus status;
    private T value;

    public List<ConnectorErrors> getErrors() {
        return errors;
    }

    public void setErrors(List<ConnectorErrors> errors) {
        this.errors = errors;
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

    public List<String> getErrorNotes() {
        return errorNotes;
    }

    public void setErrorNotes(List<String> errorNotes) {
        this.errorNotes = errorNotes;
    }
}
