package com.pereposter.social.twitter.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.TwitterException;
import com.pereposter.social.api.entity.Response;
import net.oauth.OAuthAccessor;
import net.oauth.client.httpclient4.OAuthCredentials;
import net.oauth.client.httpclient4.OAuthSchemeFactory;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
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

    @Value("${pereposter.social.twitter.client.maxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;

    @Value("${pereposter.social.twitter.client.maxTotalConnections}")
    private Integer maxTotalConnections;

    private DefaultHttpClient httpClient;

    @PostConstruct
    private void initClient() {
        ClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        ((PoolingClientConnectionManager) connectionManager).setMaxTotal(maxTotalConnections);
        ((PoolingClientConnectionManager) connectionManager).setDefaultMaxPerRoute(defaultMaxConnectionsPerHost);
        httpClient = new DefaultHttpClient(connectionManager);
    }

    @PreDestroy
    private void onDestroy() {
        httpClient.getConnectionManager().shutdown();
    }

    public Response processRequest(HttpUriRequest request, OAuthAccessor oAuthAccessor) throws TwitterException {


        httpClient.getAuthSchemes().register(AUTH.WWW_AUTH_RESP, new OAuthSchemeFactory());

        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM,
                        OAuthSchemeFactory.SCHEME_NAME),
                new OAuthCredentials(oAuthAccessor));


        Response result = null;


        try {
            HttpResponse httpResponse = httpClient.execute(request);
            result = new Response(CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8)), httpResponse);
        } catch (Exception e) {
            throw new TwitterException(e.getMessage(), e);
        }
        request.abort();

        return result;
    }
}
