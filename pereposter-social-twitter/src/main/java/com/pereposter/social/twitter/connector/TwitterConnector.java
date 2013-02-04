package com.pereposter.social.twitter.connector;

import com.pereposter.social.api.SocialNetworkConnector;
import com.pereposter.social.api.TwitterException;
import com.pereposter.social.api.entity.*;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import org.apache.commons.net.util.Base64;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.joda.time.DateTime;
import org.scribe.services.HMACSha1SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class TwitterConnector implements SocialNetworkConnector {

    @Autowired
    private Client client;

    @Override
    public ResponseObject<String> writeNewPost(SocialAuthEntity auth, PostEntity postEntity) {
        return null;
    }

    @Override
    public ResponseObject<String> writeNewPosts(SocialAuthEntity auth, List<PostEntity> postEntities) {
        return null;
    }

    @Override
    public ResponseObject<PostEntity> findPostById(SocialAuthEntity auth, String postId) {
        return null;
    }

    @Override
    public ResponseObject<PostsResponse> findPostsByOverCreatedDate(SocialAuthEntity auth, DateTime createdDate) {
        return null;
    }

    @Override
    public ResponseObject<PostEntity> findLastPost(SocialAuthEntity auth) {

        String a = Base64.encodeBase64String(UUID.randomUUID().toString().getBytes()).substring(0, 32);

        HttpPost httpGet = new HttpPost("https://api.twitter.com/oauth/request_token");


        try {
            httpGet.setEntity(new StringEntity("oauth_callback=oob"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


//        httpGet.setHeader(new BasicHeader("Authorization: OAuth", ""));
//        httpGet.setHeader(new BasicHeader("oauth_consumer_key", "SsqQKenbELpHjJo9kfAtQ"));
//        httpGet.setHeader(new BasicHeader("oauth_nonce", a));
//        httpGet.setHeader(new BasicHeader("oauth_signature",new HMACSha1SignatureService().getSignature(httpGet.getURI().toString(), "vTNDBs9eTUOEMukNj5pE5WeOoCMf39StCwYS8lxz0dg", "Nf6BpDWiqs5iDU4WI3uNoXoEEYHNJkUxyaNast18UQ")));
//        httpGet.setHeader(new BasicHeader("oauth_signature_method","HMAC-SHA1"));
//        httpGet.setHeader(new BasicHeader("oauth_timestamp", Long.toString(new DateTime().getMillis())));
//        httpGet.setHeader(new BasicHeader("oauth_token", "1086416810-W6p2SSHGkDuRKTQQ1RGbMD1v1EOplpUzw3o5eRq"));
//        httpGet.setHeader(new BasicHeader("oauth_version", "1.1"));


        OAuthServiceProvider oAuthServiceProvider = new OAuthServiceProvider("https://api.twitter.com/oauth/request_token", "https://api.twitter.com/oauth/authorize", "https://api.twitter.com/oauth/access_token");
        OAuthConsumer oAuthConsumer = new OAuthConsumer("oob","SsqQKenbELpHjJo9kfAtQ","vTNDBs9eTUOEMukNj5pE5WeOoCMf39StCwYS8lxz0dg", oAuthServiceProvider);
        OAuthAccessor oAuthAccessor = new OAuthAccessor(oAuthConsumer);
        oAuthAccessor.setProperty("oauth_timestamp", Long.toString(new DateTime().getMillis()));
        oAuthAccessor.setProperty("oauth_version", "1.0");
        oAuthAccessor.setProperty("oauth_signature_method", "HMAC-SHA1");
        oAuthAccessor.setProperty("oauth_signature", new HMACSha1SignatureService().getSignature(httpGet.getURI().toString(), "vTNDBs9eTUOEMukNj5pE5WeOoCMf39StCwYS8lxz0dg", "Nf6BpDWiqs5iDU4WI3uNoXoEEYHNJkUxyaNast18UQ"));
        oAuthAccessor.setProperty("oauth_nonce", a);

        Response response = null;

        try {

            response = client.processRequest(httpGet, oAuthAccessor);
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(response.getBody());

        return null;
    }
}
