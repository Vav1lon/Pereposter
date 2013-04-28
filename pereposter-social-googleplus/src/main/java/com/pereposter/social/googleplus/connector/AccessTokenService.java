package com.pereposter.social.googleplus.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.GooglePlusException;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.api.entity.SocialAuthEntity;
import com.pereposter.social.googleplus.entity.AccessToken;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccessTokenService {

    @Autowired
    private Client client;

    @Value("${pereposter.social.googleplus.auth_uri}")
    private String auth_uri;

    @Value("${pereposter.social.googleplus.redirect_uris}")
    private String redirect_uris;

    @Value("${pereposter.social.googleplus.clientId}")
    private String clientId;

    @Value("${pereposter.social.googleplus.scope}")
    private String scope;

    @Value("${pereposter.social.googleplus.responseType}")
    private String responseType;

    @Value("${pereposter.social.googleplus.apiKey}")
    private String apiKey;

    @Value("${pereposter.social.googleplus.url_getUserId}")
    private String urlUserId;

    public AccessToken getAccessToken(SocialAuthEntity auth) throws GooglePlusException {

        String newUrl2 = null;
        Response response = null;

        // Step 1 :: first request
        response = step1();

        if (!response.getHttpResponse().getFirstHeader("Location").getValue().contains("access_token")) {

            if (response.getHttpResponse().getStatusLine().getStatusCode() != 200) {

                if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
                    throw new GooglePlusException("Не верный ответ на первый запрос аутификации");
                }

                // Step 2 :: get url And open login page
                response = step2(response);

                // Step 3 :: Parse html and send login data
                if (response == null || Strings.isNullOrEmpty(response.getBody())) {
                    //TODO: пишем в лог
                    throw new GooglePlusException("Нет даннх о странице логина");
                }
                response = step3(response, auth);


                // Step 4 :: redirect checkCoockie
                response = step4(response);

                // step5
                response = step5(response);

                newUrl2 = response.getHttpResponse().getFirstHeader("Location").getValue();
            }

            response = step6(newUrl2);

            response = step7(response);
        }

        AccessToken accessToken = new AccessToken();

        String[] arr = response.getHttpResponse().getFirstHeader("Location").getValue().split("&");

        for (String anArr : arr) {

            if (anArr.contains("access_token")) {
                accessToken.setAccessToken(anArr.split("=")[1]);
            }

            if (anArr.contains("expires_in")) {
                accessToken.setExpiresIn(new Long(anArr.split("=")[1]));
            }

            if (anArr.contains("token_type")) {
                accessToken.setTokenType(anArr.split("=")[1]);
            }
        }

        getAndSetUserIdtiAccessToken(accessToken);

        return accessToken;
    }

    private void getAndSetUserIdtiAccessToken(AccessToken accessToken) throws GooglePlusException {
        Response response;HttpGet httpGet = new HttpGet(urlUserId);
        httpGet.addHeader(new BasicHeader("Authorization", accessToken.getTokenType() + " " + accessToken.getAccessToken()));

        response = client.processRequest(httpGet);

        accessToken.setUserId(response.getBody().split(":")[1].split("\"")[1]);
    }

    private Response step7(Response response) throws GooglePlusException {

        String par1 = readHtmlAndFindValueParam("action=\"", response.getBody()).replace("&amp;amp%3B", "&").replace("&amp;", "&");
        String par2 = readHtmlAndFindValueParam("name=\"state_wrapper\" value=\"", response.getBody());

        List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("state_wrapper", par2));
        nameValuePairs1.add(new BasicNameValuePair("submit_access", "true"));
        nameValuePairs1.add(new BasicNameValuePair("bgresponse", ""));

        HttpPost step7 = new HttpPost(par1);

        try {
            step7.setEntity(new UrlEncodedFormEntity(nameValuePairs1, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return client.processRequest(step7);
    }

    private Response step6(String newUrl2) throws GooglePlusException {
        return client.processRequest(new HttpPost(newUrl2));
    }

    private Response step5(Response response) throws GooglePlusException {
        String newUrl = readHtmlAndFindValueParam("content=\"4;url=", response.getBody()).replace("&amp;amp%3B", "&").replace("&amp;", "&");

        return client.processRequest(new HttpPost(newUrl));
    }

    private Response step4(Response response) throws GooglePlusException {

        String urlStep4 = null;
        if (response.getHttpResponse().getFirstHeader("Location") != null) {
            urlStep4 = response.getHttpResponse().getFirstHeader("Location").getValue();
        } else {
            throw new GooglePlusException("Пользователь не прошел ауйнтификацию");
        }
        return client.processRequest(new HttpPost(urlStep4));
    }

    private Response step3(Response response, SocialAuthEntity auth) throws GooglePlusException {
        String fromUrlNameParam = "action=\"";

        String continueNameParam = "continue\" value=\"";
        String serviceNameParam = "service\" value=\"";
        String dshNameParam = "dsh\" value=\"";
        String GALXNameParam = "GALX\"\n" +
                "         value=\"";
        String _utf8NameParam = "_utf8\" value=\"";


        String fromUrlValueParam = readHtmlAndFindValueParam(fromUrlNameParam, response.getBody());
        String continueValueParam = readHtmlAndFindValueParam(continueNameParam, response.getBody());
        String serviceValueParam = readHtmlAndFindValueParam(serviceNameParam, response.getBody());
        String dshValueParam = readHtmlAndFindValueParam(dshNameParam, response.getBody());
        String GALXValueParam = readHtmlAndFindValueParam(GALXNameParam, response.getBody());
        String _utf8ValueParam = readHtmlAndFindValueParam(_utf8NameParam, response.getBody());


        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("continue", continueValueParam));
        nameValuePairs.add(new BasicNameValuePair("service", serviceValueParam));
        nameValuePairs.add(new BasicNameValuePair("dsh", dshValueParam));
        nameValuePairs.add(new BasicNameValuePair("GALX", GALXValueParam));
        nameValuePairs.add(new BasicNameValuePair("_utf8", _utf8ValueParam));
        nameValuePairs.add(new BasicNameValuePair("timeStmp", ""));
        nameValuePairs.add(new BasicNameValuePair("secTok", ""));
        nameValuePairs.add(new BasicNameValuePair("pstMsg", "1"));
        nameValuePairs.add(new BasicNameValuePair("dnConn", ""));
        nameValuePairs.add(new BasicNameValuePair("checkConnection", "youtube:92:1"));
        nameValuePairs.add(new BasicNameValuePair("checkedDomains", "youtube"));
        nameValuePairs.add(new BasicNameValuePair("bgresponse", "js_disabled"));
        nameValuePairs.add(new BasicNameValuePair("checkedDomains", "youtube"));
        nameValuePairs.add(new BasicNameValuePair("PersistentCookie", "yes"));
        nameValuePairs.add(new BasicNameValuePair("Email", auth.getLogin()));
        nameValuePairs.add(new BasicNameValuePair("Passwd", auth.getPassword()));

        HttpPost step3 = new HttpPost(fromUrlValueParam);

        step3.setHeader(new BasicHeader("content-type", "application/x-www-form-urlencoded"));

        try {
            step3.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            //TODO: write to log
            e.printStackTrace();
        }
        return client.processRequest(step3);
    }

    private Response step2(Response response) throws GooglePlusException {
        return client.processRequest(new HttpPost(response.getHttpResponse().getFirstHeader("Location").getValue()));
    }

    private Response step1() throws GooglePlusException {
        StringBuffer url = new StringBuffer();

        url.append(auth_uri);
        try {
            url.append("scope=" + URLEncoder.encode(scope, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //TODO: write log
            e.printStackTrace();
        }

        url.append("&client_id=" + clientId);
        url.append("&redirect_uri=" + redirect_uris);
        url.append("&state=profile");
        url.append("&response_type=" + responseType);

        return client.processRequestCleanCookie(new HttpPost(url.toString()));
    }

    private String readHtmlAndFindValueParam(String paramName, String html) {

        int begin = html.indexOf(paramName) + paramName.length();
        int end = html.substring(begin).indexOf("\"");

        return html.substring(begin, begin + end);
    }
}
