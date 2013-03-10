package com.pereposter.social.tumblr.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.tumblr.entity.OAuthToken;
import com.pereposter.social.tumblr.entity.TumblrException;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStreamReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Client {

    @Value("${pereposter.social.tumblr.client.maxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;

    @Value("${pereposter.social.tumblr.client.maxTotalConnections}")
    private Integer maxTotalConnections;

    private DefaultHttpClient httpClient;

    private CommonsHttpOAuthProvider oAuthProvider;

    @PostConstruct
    private void initClient() {
        ClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        ((PoolingClientConnectionManager) connectionManager).setMaxTotal(maxTotalConnections);
        ((PoolingClientConnectionManager) connectionManager).setDefaultMaxPerRoute(defaultMaxConnectionsPerHost);
        httpClient = new DefaultHttpClient(connectionManager);

        oAuthProvider = new CommonsHttpOAuthProvider("http://www.tumblr.com/oauth/request_token"
                , "http://www.tumblr.com/oauth/access_token"
                , "http://www.tumblr.com/oauth/authorize");

        oAuthProvider.setOAuth10a(true);
        oAuthProvider.setHttpClient(httpClient);
    }

    @PreDestroy
    private void onDestroy() {
        httpClient.getConnectionManager().shutdown();
    }

    public Response processRequest(HttpUriRequest request) throws TumblrException {

        Response result = null;
        HttpResponse httpResponse = null;

        //httpClient.getCookieStore().clear();

        try {
            httpResponse = httpClient.execute(request);
            result = new Response(CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8)), httpResponse);
        } catch (Exception e) {
            throw new TumblrException(e.getMessage(), e);
        }
        request.abort();

        return result;
    }

    public Response processRequest(HttpUriRequest request, boolean flag) throws TumblrException {

        Response result = null;
        HttpResponse httpResponse = null;

        if (flag) {
            httpClient.getCookieStore().clear();
        }

        try {
            httpResponse = httpClient.execute(request);
            result = new Response(CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8)), httpResponse);
        } catch (Exception e) {
            throw new TumblrException(e.getMessage(), e);
        }
        request.abort();

        return result;
    }

    public OAuthToken processRequestToken(OAuthConsumer consumer) throws TumblrException {

        OAuthToken result = new OAuthToken();

        try {
            oAuthProvider.retrieveRequestToken(consumer, "localhost");
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthNotAuthorizedException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }

        result.setOauthToken(consumer.getToken());
        result.setOauthTokenSecret(consumer.getTokenSecret());

        return result;
    }


    public Response processAccessToken(OAuthConsumer consumer, String oauthVerifier) throws TumblrException {

        Response result = null;
        HttpResponse httpResponse = null;

        httpClient.getCookieStore().clear();

        try {
            oAuthProvider.retrieveAccessToken(consumer, oauthVerifier);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OAuthNotAuthorizedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        HttpPost write = new HttpPost("http://api.tumblr.com/v2/blog/pereposter.tumblr.com/post?type=text&state=published&title=testJava&body=HoHoHoBo&api_key=s66LT5sOFgoFtbVw1ePkPjqssrYwYFfibWkE304xcOmVS2Upcv");

        try {
            httpResponse = httpClient.execute(write);
            result = new Response(CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8)), httpResponse);
        } catch (Exception e) {
            throw new TumblrException(e.getMessage(), e);
        }
        write.abort();

        return result;
    }

}
