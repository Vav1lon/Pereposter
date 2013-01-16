package com.pereposter.social.api.connector;

import com.pereposter.social.entity.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

public interface SocialNetworkClient {

    HttpResponse sendRequestReturnHttpResponse(HttpUriRequest request);

    String sendRequestReturnBody(HttpUriRequest request);

    Response sendRequestReturnBodyAndResponse(HttpUriRequest request, boolean clearCookie);

}
