package com.pereposter.boundary;

import com.pereposter.control.SocialNetworkControl;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.service.PostManager;
import com.pereposter.social.api.connector.SocialNetworkConnector;
import com.pereposter.social.entity.SocialAuth;
import com.pereposter.social.vkontakte.connector.VkontakteConnector;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@WebService
@Component
@Path("/")
@Transactional
public class TestBoundary {

    @Autowired
    private SocialNetworkConnector facebookConnector;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PostManager postManager;

    @GET
    public void test() {

        postManager.findAndWriteNewPost();


//        SocialAuth socialAuth = new SocialAuth();
//        socialAuth.setPassword("A329k4219516811");
//        socialAuth.setLogin("denis.kuzmin.7758@facebook.com");
//
//        facebookConnector.writeNewPost(socialAuth, null);

        //postManager.findAndWriteNewPost();

    }

}
