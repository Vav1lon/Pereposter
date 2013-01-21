package com.pereposter.social;

import com.pereposter.social.api.Constants;
import com.pereposter.social.api.entity.RequestStatus;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.Header;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ResponseHolder<T extends Serializable> {

    //todo potential memory leak. fix it
    private Map<String, T> responses = new HashMap<String, T>();

    private Set<String> pendingRequests = new HashSet<String>();

    @Handler
    public synchronized void addResponse(@Header(Constants.REQUEST_ID) String requestId, @Body T response) {
        if (pendingRequests.contains(requestId)) {
            responses.put(requestId, response);
        }
    }

    public synchronized RequestStatus getRequestStatus(@Body String requestId) {
        if (responses.get(requestId) == null) {
            if (pendingRequests.contains(requestId)) {
                return RequestStatus.PENDING;
            } else {
                return RequestStatus.NOT_FOUND;
            }
        } else {
            return RequestStatus.READY;
        }
    }

    public synchronized T getResponse(@Header(Constants.REQUEST_ID) String requestId) {
        //todo fix trim hack here
        T remove = responses.remove(requestId.trim());
        pendingRequests.remove(requestId.trim());
        return remove;
    }

    public synchronized void waitForResponse(String requestId) {
        pendingRequests.add(requestId);
    }

    public synchronized void cancelRequest(String requestId) {
        pendingRequests.remove(requestId);
        responses.remove(requestId);

    }

}
