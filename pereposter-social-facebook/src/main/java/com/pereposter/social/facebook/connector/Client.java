package com.pereposter.social.facebook.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.FacebookException;
import com.pereposter.social.api.entity.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Client {

    @Value("${pereposter.social.facebook.client.maxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;

    @Value("${pereposter.social.facebook.client.maxTotalConnections}")
    private Integer maxTotalConnections;

    private DefaultHttpClient httpClient;

    @PostConstruct
    private void initClient() {
        ClientConnectionManager connectionManager = new PoolingClientConnectionManager();
        ((PoolingClientConnectionManager) connectionManager).setMaxTotal(maxTotalConnections);
        ((PoolingClientConnectionManager) connectionManager).setDefaultMaxPerRoute(defaultMaxConnectionsPerHost);
        httpClient = new DefaultHttpClient(connectionManager);
    }

    public Response processRequest(HttpUriRequest request) throws FacebookException {
        HttpResponse httpResponse = null;
        String bodyResponse = null;

        try {
            httpResponse = httpClient.execute(request);
            bodyResponse = CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8));
        } catch (Exception e) {
            throw new FacebookException(e.getMessage(), e);
        }
        request.abort();

        return new Response(bodyResponse, httpResponse);
    }

}
