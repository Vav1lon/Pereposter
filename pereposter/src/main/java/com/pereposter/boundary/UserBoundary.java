package com.pereposter.boundary;

import com.pereposter.control.SocialNetworkControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@WebService
@Component
@Path("/User")
@Transactional
public class UserBoundary {

    @Autowired
    private SocialNetworkControl socialNetworkControl;

    @GET
    @Path("/{id}")
    public void initUser(@PathParam("id") String id) {
        socialNetworkControl.initializationUser(id);
    }

}
