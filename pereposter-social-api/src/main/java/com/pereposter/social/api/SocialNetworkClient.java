package com.pereposter.social.api;

import com.pereposter.social.api.entity.Response;
import org.apache.http.client.methods.HttpUriRequest;

public interface SocialNetworkClient {

    Response processRequest(HttpUriRequest request, boolean clearCookie);

}
