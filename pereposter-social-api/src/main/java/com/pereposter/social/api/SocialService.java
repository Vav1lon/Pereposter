package com.pereposter.social.api;

import com.pereposter.social.api.entity.RequestStatus;

import javax.jws.WebService;

@WebService
public interface SocialService {

    RequestStatus getStatus(String requestId);

    void cancelRequest(String requestId);

}
