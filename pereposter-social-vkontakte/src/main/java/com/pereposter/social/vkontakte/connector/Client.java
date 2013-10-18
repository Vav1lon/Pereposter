package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.api.social.VkontakteException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
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

    @Value("${pereposter.social.vkontakte.client.maxConnectionsPerHost}")
    private Integer defaultMaxConnectionsPerHost;

    @Value("${pereposter.social.vkontakte.client.maxTotalConnections}")
    private Integer maxTotalConnections;

    private HttpClient httpClient;

    @PostConstruct
    private void initClient() {
        httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(maxTotalConnections)
                .setMaxConnPerRoute(defaultMaxConnectionsPerHost)
                .build();
    }

    public Response processRequest(HttpUriRequest request, boolean clearCookie) throws VkontakteException {

        Response result;
        HttpResponse httpResponse;
        HttpClientContext context = HttpClientContext.create();

        try {
            if (clearCookie) {
                httpResponse = httpClient.execute(request, context);
            } else {
                httpResponse = httpClient.execute(request, context);
            }

            result = new Response(CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8)), httpResponse);
        } catch (Exception e) {
            throw new VkontakteException(e.getMessage(), e);
        }
        request.abort();

        return result;
    }
}
