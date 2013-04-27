package com.pereposter.social.googleplus.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.GooglePlusException;
import com.pereposter.social.api.entity.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccessTokenService {

    @Autowired
    private Client client;

    private String auth_uri = "https://accounts.google.com/o/oauth2/auth?";
    private String redirect_uris = "http://localhost/callback";
    private String clientId = "843096996013.apps.googleusercontent.com";
    private String scope = "https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
    private String responseType = "token";
    private String state = "profile";


    public void getAccessToken() throws GooglePlusException {

        String newUrl2 = null;
        Response response = null;

        // Step 1 :: first request

        StringBuilder urlStep1 = new StringBuilder();

        urlStep1.append(auth_uri);
        try {
            urlStep1.append("scope=" + URLEncoder.encode(scope, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //TODO: write log
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        urlStep1.append("&client_id=" + clientId);
        urlStep1.append("&redirect_uri=" + redirect_uris);
        urlStep1.append("&state=profile");
        urlStep1.append("&response_type=" + responseType);

        HttpPost step1 = new HttpPost(urlStep1.toString());


        response = client.processRequest(step1);

        if (response.getHttpResponse().getStatusLine().getStatusCode() != 200) {

            if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
                throw new GooglePlusException("Не верный ответ на первый запрос аутификации");
            }

            //Step 2 :: get url And open login page

            String urlStep2 = response.getHttpResponse().getFirstHeader("Location").getValue();

            HttpPost step2 = new HttpPost(urlStep2);

            try {
                response = client.processRequest(step2);
            } catch (GooglePlusException e) {
                //TODO: пишем в лог
                e.printStackTrace();
            }


            // Step 3 :: Parse html and send login data

            if (response == null || Strings.isNullOrEmpty(response.getBody())) {
                //TODO: пишем в лог
                throw new GooglePlusException("Нет даннх о странице логина");
            }

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
            nameValuePairs.add(new BasicNameValuePair("Email", "pereposter@gmail.com"));
            nameValuePairs.add(new BasicNameValuePair("Passwd", "19516811"));

            HttpPost step3 = new HttpPost(fromUrlValueParam);

            step3.setHeader(new BasicHeader("content-type", "application/x-www-form-urlencoded"));

            try {
                step3.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            response = client.processRequest(step3);


            // Step 4 :: redirect checkCoockie

            String urlStep4 = response.getHttpResponse().getFirstHeader("Location").getValue();

            if (Strings.isNullOrEmpty(urlStep4)) {
                throw new GooglePlusException("Пользователь не прошел ауйнтификацию");
            }

            HttpPost step4 = new HttpPost(urlStep4);
            response = client.processRequest(step4);

            // next


            String newUrl = readHtmlAndFindValueParam("content=\"4;url=", response.getBody()).replace("&amp;amp%3B", "&").replace("&amp;", "&");


            HttpPost step5 = new HttpPost(newUrl);
            response = client.processRequest(step5);

            newUrl2 = response.getHttpResponse().getFirstHeader("Location").getValue();

        }

        HttpPost step6 = new HttpPost(newUrl2);
        response = client.processRequest(step6);

        String par1 = readHtmlAndFindValueParam("form action=\"", response.getBody()).replace("&amp;amp%3B", "&").replace("&amp;", "&");
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

        response = client.processRequest(step7);

        System.out.println(response.getHttpResponse().getFirstHeader("Location").getValue());


    }

    private String readHtmlAndFindValueParam(String paramName, String html) {

        int begin = html.indexOf(paramName) + paramName.length();
        int end = html.substring(begin).indexOf("\"");

        return html.substring(begin, begin + end);
    }
}
