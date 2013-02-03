package com.pereposter.boundary;

import com.pereposter.entity.RestResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;

@WebService
@Transactional
public interface SocialBoundary {

    RestResponse initSocialAccount(String id);

}
