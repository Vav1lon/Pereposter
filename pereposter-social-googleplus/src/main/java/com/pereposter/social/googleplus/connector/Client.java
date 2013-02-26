package com.pereposter.social.googleplus.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.GooglePlusException;
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
import javax.annotation.PreDestroy;
import java.io.InputStreamReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Client {

    @Value("${pereposter.social.googleplus.client.maxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;

    @Value("${pereposter.social.googleplus.client.maxTotalConnections}")
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

    public Response processRequest(HttpUriRequest request) throws GooglePlusException {

        Response result = null;

        try {
            HttpResponse httpResponse = httpClient.execute(request);
            result = new Response(CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8)), httpResponse);
        } catch (Exception e) {
            throw new GooglePlusException(e.getMessage(), e);
        }
        request.abort();

        return result;
    }
}
