package com.pereposter.social.facebook.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.api.social.FacebookException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
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


    private HttpClient httpClient;

    @PostConstruct
    private void initClient() {
        httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(maxTotalConnections)
                .setMaxConnPerRoute(defaultMaxConnectionsPerHost)
                .build();
    }

    public Response processRequest(HttpUriRequest request) throws FacebookException {
        HttpResponse httpResponse;
        String bodyResponse;

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
