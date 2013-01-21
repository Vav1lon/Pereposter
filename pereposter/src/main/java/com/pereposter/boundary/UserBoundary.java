package com.pereposter.boundary;

import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;

@WebService
@Transactional
public interface UserBoundary {

    void initUser(String id);

}
