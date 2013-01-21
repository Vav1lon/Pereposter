package com.pereposter.boundary.impl;

import com.pereposter.boundary.UserBoundary;
import com.pereposter.control.SocialNetworkControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Component("userBoundary")
@Path("/User")
public class UserBoundaryImpl implements UserBoundary {

    @Autowired
    private SocialNetworkControl socialNetworkControl;

    @GET
    @Path("/{id}")
    @Override
    public void initUser(@PathParam("id") String id) {
        socialNetworkControl.initializationUser(id);
    }

}
