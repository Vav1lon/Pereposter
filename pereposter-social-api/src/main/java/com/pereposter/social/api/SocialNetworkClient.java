package com.pereposter.social.api;

import com.pereposter.social.api.entity.Response;
import org.apache.http.client.methods.HttpUriRequest;

public interface SocialNetworkClient {

    Response sendRequestReturnBodyAndResponse(HttpUriRequest request, boolean clearCookie);

}
