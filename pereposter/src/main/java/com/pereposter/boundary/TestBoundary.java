package com.pereposter.boundary;

import com.pereposter.control.SocialNetworkControl;
import com.pereposter.service.PostManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@WebService
@Component
@Path("/")
public class TestBoundary {

    @Autowired
    private PostManager postManager;

    @Autowired
    private SocialNetworkControl socialNetworkControl;

    @GET
    public void test() {

        postManager.findAndWriteNewPost();

    }

}
