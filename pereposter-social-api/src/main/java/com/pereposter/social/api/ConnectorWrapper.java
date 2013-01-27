package com.pereposter.social.api;

import java.util.Set;
import java.util.TreeSet;

public class ConnectorWrapper {

    private Set<String> requestIds = new TreeSet<String>();

    public void cancelRequest(String requestId) {
        requestIds.add(requestId);
    }

    public boolean isRequestCanceled(String requestId) {
        return requestIds.remove(requestId);
    }

}
