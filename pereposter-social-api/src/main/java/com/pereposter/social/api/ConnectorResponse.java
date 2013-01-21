package com.pereposter.social.api;

import java.io.Serializable;

public class ConnectorResponse implements Serializable {

    private ResponseStatusInfo statusInfo = ResponseStatusInfo.OK;

    public ResponseStatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(ResponseStatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

}
