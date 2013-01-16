package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.pereposter.social.api.connector.SocialNetworkClient;
import com.pereposter.social.entity.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;

@Component("vkontakteClient")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Client implements SocialNetworkClient {

    private DefaultHttpClient httpClient;

    @PostConstruct
    private void setUp() {
        httpClient = new DefaultHttpClient();
    }

    @Override
    public HttpResponse sendRequestReturnHttpResponse(HttpUriRequest request) {

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(request);
        } catch (Exception e) {
            //TODO: писать об ошибке в лог
            System.out.println(e.getMessage());
        }
        request.abort();

        return httpResponse;

    }

    @Override
    public String sendRequestReturnBody(HttpUriRequest request) {

        HttpResponse httpResponse;
        String bodyResponse = null;

        try {
            httpResponse = httpClient.execute(request);
            bodyResponse = CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8));
        } catch (Exception e) {
            //TODO: писать об ошибке в лог
        }
        request.abort();

        return bodyResponse;

    }

    @Override
    public Response sendRequestReturnBodyAndResponse(HttpUriRequest request, boolean clearCookie) {
        HttpResponse httpResponse = null;
        String bodyResponse = null;

        if (clearCookie) {
            httpClient.getCookieStore().clear();
        }

        try {
            httpResponse = httpClient.execute(request);
            bodyResponse = CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8));
        } catch (Exception e) {
            //TODO: писать об ошибке в лог
        }
        request.abort();

        Response result = new Response();
        result.setBody(bodyResponse);
        result.setHttpResponse(httpResponse);

        return result;
    }
}
