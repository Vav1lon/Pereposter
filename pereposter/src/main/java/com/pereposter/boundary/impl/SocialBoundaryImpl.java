package com.pereposter.boundary.impl;

import com.pereposter.boundary.SocialBoundary;
import com.pereposter.control.SocialNetworkControl;
import com.pereposter.entity.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

@Component("socialBoundary")
@Path("/social")
@Produces("application/json")
@Consumes("application/json")
public class SocialBoundaryImpl implements SocialBoundary {

    @Autowired
    private SocialNetworkControl socialNetworkControl;

    @GET
    @Path("/account/{id}")
    @Override
    public RestResponse initSocialAccount(@PathParam("id") String id) {
        return socialNetworkControl.initializationSocialAccount(id);
    }

}
