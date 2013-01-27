package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.SocialNetworkClient;
import com.pereposter.social.api.entity.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStreamReader;

@Component("vkontakteClient")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Client implements SocialNetworkClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(Client.class);

    @Value("${pereposter.social.vkontakte.client.maxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;

    @Value("${pereposter.social.vkontakte.client.maxTotalConnections}")
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

    @Override
    public Response processRequest(HttpUriRequest request, boolean clearCookie) {

        Response result = null;
        HttpResponse httpResponse = null;

        if (clearCookie) {
            httpClient.getCookieStore().clear();
        }

        try {
            httpResponse = httpClient.execute(request);
            result = new Response(CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8)), httpResponse);
        } catch (Exception e) {
            //TODO: писать об ошибке в лог
            LOGGER.error("Ошибка в работе httpclient", e);
        }
        request.abort();

        return result;
    }
}
