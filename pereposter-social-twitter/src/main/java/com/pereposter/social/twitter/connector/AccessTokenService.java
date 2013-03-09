package com.pereposter.social.twitter.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.twitter.entity.AccessToken;
import com.pereposter.social.twitter.entity.OAuthToken;
import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.HttpRequestAdapter;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class AccessTokenService {

    @Autowired
    private Client client;

    private String email = "pereposter@lenta.ru";
    private String password = "19516811";

    private String oauth_key = "8TLsVwIGPxj6qykpMyP7A";
    private String oauth_key_secret = "ZrRUHMIbnsz5zmLdqEQI4BOQkIRV7OhNz3HSc4K24";
    private String callback_URL = "http://www.pereposter.ru/callback/twitter";

    private String oauth_token = "1086416810-bh67nLxMqdf7dL8udOR1mlqgLQz61e6TM0Rl6gl";
    private String oauth_token_secret = "nqZh3gRTV2cFiDxd1jHiondIVjVjnomScCjRPlY0g";

    private String request_token_URL = "https://api.twitter.com/oauth/request_token";
    private String authorize_URL = "https://api.twitter.com/oauth/authorize?force_login=true&oauth_token=";
    private String accessTokenURL = "https://api.twitter.com/oauth/access_token";

    private static final String FINAL_CHAR = "\"";

    public void getAccessToken() {

        Response response = null;
        OAuthToken oauthToken = new OAuthToken();
        oauthToken.setOauthToken(oauth_token);
        oauthToken.setOauthTokenSecret(oauth_token_secret);

        String urlOAuthTokenOAuthTokenSecret = null;

        //Step 1 :: get oauth params

        final Random random = new Random(System.nanoTime());

        HttpPost post0 = new HttpPost(request_token_URL);

        HmacSha1MessageSigner signer = new HmacSha1MessageSigner();
        signer.setConsumerSecret(oauth_key_secret);

        String once = Long.toString(random.nextLong());
        String timestamp = Long.toString(System.currentTimeMillis() / 1000L);

        HttpParameters httpParameters = new HttpParameters();
        httpParameters.put("oauth_callback", "http%3A%2F%2Fwww.pereposter.ru%2Fcallback%2Ftwitter");
        httpParameters.put("oauth_consumer_key", oauth_key);
        httpParameters.put("oauth_nonce", once);
        httpParameters.put("oauth_signature_method", signer.getSignatureMethod());
        httpParameters.put("oauth_timestamp", timestamp);
        httpParameters.put("oauth_version", "1.0");

        HttpRequest httpRequest = new HttpRequestAdapter(post0);

        String sign = null;
        try {
            sign = signer.sign(httpRequest, httpParameters);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        AuthorizationHeaderSigningStrategy authorizationHeaderSigningStrategy = new AuthorizationHeaderSigningStrategy();
        String authHeaderValue = authorizationHeaderSigningStrategy.writeSignature(sign, httpRequest, httpParameters);

        post0.setHeader(new BasicHeader("Authorization", authHeaderValue));

        try {
            response = client.processRequest(post0);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        if (!Strings.isNullOrEmpty(response.getBody())) {

            Map<String, String> paramValueMap = parseParamInUrl(response.getBody());

            if (paramValueMap.containsKey("oauth_token")) {
                oauthToken.setOauthToken(paramValueMap.get("oauth_token"));
            }

            if (paramValueMap.containsKey("oauth_token_secret")) {
                oauthToken.setOauthTokenSecret(paramValueMap.get("oauth_token_secret"));
            }

            if (response.getBody().contains("oauth_token") && response.getBody().contains("oauth_token_secret")) {
                urlOAuthTokenOAuthTokenSecret = response.getBody();
            }

        }


        //Step 2 :: load login page
        HttpPost post1 = new HttpPost(authorize_URL + oauthToken.getOauthToken());
        try {
            response = client.processRequest(post1);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        AccessToken accessToken = new AccessToken();

        if (!Strings.isNullOrEmpty(response.getBody())) {

            // Step 3 :: get param from login page

            String authenticity_tokenParamName = "name=\"authenticity_token\" type=\"hidden\" value=\"";
            String oauth_tokenParamName = "name=\"oauth_token\" type=\"hidden\" value=\"";
            String actionParamName = "action=\"";

            String authenticity_tokenParamValue = getValueFromHtml(authenticity_tokenParamName, response.getBody(), FINAL_CHAR);
            String oauth_tokenParamValue = getValueFromHtml(oauth_tokenParamName, response.getBody(), FINAL_CHAR);
            String actionParamValue = getValueFromHtml(actionParamName, response.getBody(), FINAL_CHAR);


            ArrayList<BasicNameValuePair> bodyParams = new ArrayList<BasicNameValuePair>();
            bodyParams.add(new BasicNameValuePair("session[username_or_email]", email));
            bodyParams.add(new BasicNameValuePair("session[password]", password));
            bodyParams.add(new BasicNameValuePair("oauth_token", oauth_tokenParamValue));
            bodyParams.add(new BasicNameValuePair("authenticity_token", authenticity_tokenParamValue));
            bodyParams.add(new BasicNameValuePair("remember_me", "0"));

            HttpPost post2 = new HttpPost(actionParamValue);

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(bodyParams);
                post2.setEntity(entity);
                try {
                    response = client.processRequest(post2);
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } catch (UnsupportedEncodingException e) {
            }


            // Step 4 :: get verifier code

            String verifierParamName = "<a href=\"http://www.pereposter.ru/callback/twitter?";
            String verifierParamValue = getValueFromHtml(verifierParamName, response.getBody(), FINAL_CHAR);


            String[] tokens = verifierParamValue.split("&");
            oauthToken.setOauthVerifier(tokens[1].substring(tokens[1].indexOf("=") + 1));

            // Step 5 :: request access code


            ArrayList<BasicNameValuePair> xauth_params = new ArrayList<BasicNameValuePair>();
            xauth_params.add(new BasicNameValuePair("oauth_verifier", oauthToken.getOauthVerifier()));

            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(xauth_params);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            signer.setTokenSecret(oauthToken.getOauthTokenSecret());

            once = Long.toString(random.nextLong());
            timestamp = Long.toString(System.currentTimeMillis() / 1000L);

            httpParameters = new HttpParameters();
            httpParameters.put("oauth_consumer_key", oauth_key);
            httpParameters.put("oauth_nonce", once);
            httpParameters.put("oauth_signature_method", signer.getSignatureMethod());
            httpParameters.put("oauth_timestamp", timestamp);
            httpParameters.put("oauth_token", oauthToken.getOauthToken());
            httpParameters.put("oauth_version", "1.0");


            try {
                sign = signer.sign(httpRequest, httpParameters);
            } catch (OAuthMessageSignerException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            authorizationHeaderSigningStrategy = new AuthorizationHeaderSigningStrategy();
            authHeaderValue = authorizationHeaderSigningStrategy.writeSignature(sign, httpRequest, httpParameters);


            HttpPost post3 = new HttpPost(accessTokenURL);

            post3.setEntity(entity);

            post3.setHeader(new BasicHeader(OAuth.HTTP_AUTHORIZATION_HEADER, authHeaderValue));
            post3.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
            post3.setHeader(new BasicHeader("Accept", "*/*"));

            try {
                response = client.processRequest(post3);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            String[] accessTokens = response.getBody().split("&");


            if (!Strings.isNullOrEmpty(response.getBody())) {

                Map<String, String> paramValueMap = parseParamInUrl(response.getBody());

                if (paramValueMap.containsKey("oauth_token")) {
                    accessToken.setAccessToken(paramValueMap.get("oauth_token"));
                }

                if (paramValueMap.containsKey("oauth_token_secret")) {
                    accessToken.setAccessTokenSecret(paramValueMap.get("oauth_token_secret"));
                }

                if (paramValueMap.containsKey("user_id")) {
                    accessToken.setUserId(paramValueMap.get("user_id"));
                }

            }


        }


        // Step test write

        HttpPost write = new HttpPost("https://api.twitter.com/1.1/statuses/update.json");
        write.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        write.setHeader(new BasicHeader("Accept", "application/json, application/*+json"));


        httpRequest = new HttpRequestAdapter(write);

        signer = new HmacSha1MessageSigner();

        signer.setConsumerSecret(oauth_key_secret);
        signer.setTokenSecret(accessToken.getAccessTokenSecret());

        once = Long.toString(random.nextLong());
        timestamp = Long.toString(System.currentTimeMillis() / 1000L);

        String message = null;
        try {
            message = encode("Hello Mir =)");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        httpParameters = new HttpParameters();
        httpParameters.put("status", message);
        httpParameters.put("oauth_consumer_key", oauth_key);
        httpParameters.put("oauth_nonce", once);
        httpParameters.put("oauth_signature_method", signer.getSignatureMethod());
        httpParameters.put("oauth_timestamp", timestamp);
        httpParameters.put("oauth_token", accessToken.getAccessToken());
        httpParameters.put("oauth_version", "1.0");


        try {
            sign = signer.sign(httpRequest, httpParameters);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        authorizationHeaderSigningStrategy = new AuthorizationHeaderSigningStrategy();
        authHeaderValue = authorizationHeaderSigningStrategy.writeSignature(sign, httpRequest, httpParameters);


        String a = "status=" + message;


        BasicHttpEntity entity = null;
        try {
            entity = new BasicHttpEntity();
            entity.setContent(new ByteArrayInputStream(a.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        write.setEntity(entity);

        write.setHeader(new BasicHeader(OAuth.HTTP_AUTHORIZATION_HEADER, authHeaderValue));


        try {
            response = client.processRequest(write, true);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("sdfsd");
    }

    private Map<String, String> parseParamInUrl(String url) {

        Map<String, String> result = new HashMap<String, String>();

        String workString = null;

        int firstPoint = url.indexOf("?");
        if (firstPoint != -1) {
            workString = url.substring(firstPoint);
        } else {
            workString = url;
        }

        int point;

        while (workString.contains("&")) {

            point = workString.indexOf("&");
            String paramValue = workString.substring(0, point);

            result.put(parseNameParam(paramValue), parseValueParam(paramValue));

            workString = workString.substring(point + 1);
        }


        return result;
    }

    private String parseValueParam(String paramValue) {
        return paramValue.substring(paramValue.indexOf("=") + 1);
    }

    private String parseNameParam(String paramValue) {
        return paramValue.substring(0, paramValue.indexOf("="));
    }

    private String getValueFromHtml(String nameParam, String body, String finalChar) {
        int beginIdx = body.indexOf(nameParam) + nameParam.length();
        int endIdx = body.substring(beginIdx).indexOf(finalChar);
        return body.substring(beginIdx, beginIdx + endIdx);
    }

    public static String encode(String url) throws UnsupportedEncodingException {
        String[] urlElements = url.split("/");
        String result = "";
        for (String s : urlElements) {
            result += URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("%3A", ":") + "/";
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

}


