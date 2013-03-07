package com.pereposter.social.tumblr.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.tumblr.entity.AccessToken;
import com.pereposter.social.tumblr.entity.OAuthToken;
import com.pereposter.social.tumblr.entity.TumblrException;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.HttpRequestAdapter;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component("tumblrAccessTokenService")
public class AccessTokenService {

    @Autowired
    private Client client;

    private String email = "pereposter@gmail.com";
    private String password = "19516811";
    private String blog = "pereposter.tumblr.com";
    private OAuthConsumer consumer;
    private String oauth_key = "s66LT5sOFgoFtbVw1ePkPjqssrYwYFfibWkE304xcOmVS2Upcv";
    private String oauth_secret = "37jhofXMFALCk0m5rNknqqnbYAoW3RJ2Z2ElUWZ7wFayWpjrSg";

    public AccessToken getAccessToken() {

        consumer = new CommonsHttpOAuthConsumer(oauth_key, oauth_secret);
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());

        try {
            getOAuthTokens();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("asa");

        return null;
    }

    private String[] getOAuthTokens() throws OAuthMessageSignerException,
            OAuthExpectationFailedException, OAuthCommunicationException,
            IOException {

        Response response = null;
        OAuthToken oauthToken = new OAuthToken();
        String urlOAuthTokenOAuthTokenSecret = null;

        // Step 1 :: request oauth_token

//        try {
//            oAuthToken = client.processRequestToken(consumer);
//        } catch (TumblrException e) {
//            e.printStackTrace();
//        }

//        consumer.setTokenWithSecret(oAuthToken.getOauthToken(), oAuthToken.getOauthTokenSecret());

        final Random random = new Random(System.nanoTime());

        HttpPost post0 = new HttpPost("http://www.tumblr.com/oauth/request_token");

        HmacSha1MessageSigner signer = new HmacSha1MessageSigner();
        signer.setConsumerSecret(oauth_secret);

        String once = Long.toString(random.nextLong());
        String timestamp = Long.toString(System.currentTimeMillis() / 1000L);

        HttpParameters httpParameters = new HttpParameters();
        httpParameters.put("oauth_callback", "localhost");
        httpParameters.put("oauth_consumer_key", oauth_key);
        httpParameters.put("oauth_nonce", once);
        httpParameters.put("oauth_signature_method", signer.getSignatureMethod());
        httpParameters.put("oauth_timestamp", timestamp);
        httpParameters.put("oauth_version", "1.0");

        HttpRequest httpRequest = new HttpRequestAdapter(post0);

        String sign = signer.sign(httpRequest, httpParameters);

        AuthorizationHeaderSigningStrategy authorizationHeaderSigningStrategy = new AuthorizationHeaderSigningStrategy();
        String authHeaderValue = authorizationHeaderSigningStrategy.writeSignature(sign, httpRequest, httpParameters);

        post0.setHeader(new BasicHeader(OAuth.HTTP_AUTHORIZATION_HEADER, authHeaderValue));

        try {
            response = client.processRequest(post0);
        } catch (TumblrException e) {
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

        //Step 2 :: request login link

        HttpPost post1 = new HttpPost("http://www.tumblr.com/oauth/authorize?" + urlOAuthTokenOAuthTokenSecret);
        try {
            response = client.processRequest(post1);
        } catch (TumblrException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (Strings.isNullOrEmpty(response.getBody())) {
            //Step 3 :: login

            HttpPost post2 = new HttpPost(response.getHttpResponse().getFirstHeader("Location").getValue());
            try {
                response = client.processRequest(post2);
            } catch (TumblrException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            // Step 4 :: get param from page

            String form_keyParamName = "form_key\" value=\"";
            String redirect_toParamName = "redirect_to\" value=\"";
            String recaptcha_public_keyParamName = "recaptcha_public_key\" value=\"";
            String data_secure_actionParamName = "data-secure-action=\"";

            String form_keyParamValue = getValueFromHtml(form_keyParamName, response.getBody());
            String redirect_toParamValue = getValueFromHtml(redirect_toParamName, response.getBody());
            String recaptcha_public_keyParamValue = getValueFromHtml(recaptcha_public_keyParamName, response.getBody());
            String data_secure_actionParamValue = getValueFromHtml(data_secure_actionParamName, response.getBody());


            ArrayList<BasicNameValuePair> bodyParams = new ArrayList<BasicNameValuePair>();
            bodyParams.add(new BasicNameValuePair("user[email]", email));
            bodyParams.add(new BasicNameValuePair("user[password]", password));
            bodyParams.add(new BasicNameValuePair("recaptcha_public_key", recaptcha_public_keyParamValue));
            bodyParams.add(new BasicNameValuePair("redirect_to", redirect_toParamValue));
            bodyParams.add(new BasicNameValuePair("form_key", form_keyParamValue));

            bodyParams.add(new BasicNameValuePair("tumblelog[name]", ""));
            bodyParams.add(new BasicNameValuePair("user[age]", ""));

            HttpPost post3 = new HttpPost(data_secure_actionParamValue);

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(bodyParams);
                post3.setEntity(entity);
                try {
                    response = client.processRequest(post3);
                } catch (TumblrException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } catch (UnsupportedEncodingException e) {
            }


            // Step 5 :: authorize

            String refreshParamName = "Refresh\" content=\"0;url=";
            String refreshParamValue = getValueFromHtml(refreshParamName, response.getBody());

            HttpPost post5 = new HttpPost(refreshParamValue);

            try {
                response = client.processRequest(post5);
            } catch (TumblrException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        // Step 6 :: send allow

        String form_keyParamName = "form_key\" value=\"";
        String oauth_tokenParamName = "oauth_token\" value=\"";
        String form_keyParamValue = getValueFromHtml(form_keyParamName, response.getBody());
        String oauth_tokenParamValue = getValueFromHtml(oauth_tokenParamName, response.getBody());


        ArrayList<BasicNameValuePair> bodyParams1 = new ArrayList<BasicNameValuePair>();
        bodyParams1.add(new BasicNameValuePair("form_key", form_keyParamValue));
        bodyParams1.add(new BasicNameValuePair("oauth_token", oauth_tokenParamValue));
        bodyParams1.add(new BasicNameValuePair("allow", ""));

        HttpPost post6 = new HttpPost("http://www.tumblr.com/oauth/authorize?oauth_token=" + oauthToken.getOauthToken()
                + "&oauth_token_secret=" + oauthToken.getOauthTokenSecret()
                + "&oauth_callback_confirmed=true");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(bodyParams1);
            post6.setEntity(entity);
            try {
                response = client.processRequest(post6);
            } catch (TumblrException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (UnsupportedEncodingException e) {
        }

        String[] tokens = response.getHttpResponse().getFirstHeader("Location").getValue().split("&");


        oauthToken.setOauthVerifier(tokens[1].substring(tokens[1].indexOf("=") + 1));

//        OAuthProvider oAuthProvider = new CommonsHttpOAuthProvider("http://www.tumblr.com/oauth/request_token"
//                , "http://www.tumblr.com/oauth/access_token"
//                , "http://www.tumblr.com/oauth/authorize");
//
//        try {
//            oAuthProvider.retrieveAccessToken(consumer,oAuthToken.getOauthVerifier());
//        } catch (OAuthNotAuthorizedException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


//        HttpParameters httpParameters = new HttpParameters();
//
//        httpParameters.put("oauth_verifier", oAuthToken.getOauthVerifier());
//
//        consumer.setAdditionalParameters(httpParameters);
//
////        OAuth.OAUTH_SIGNATURE_METHOD
//
//        ArrayList<BasicNameValuePair> xauth_params = new ArrayList<BasicNameValuePair>();
////        xauth_params.add(new BasicNameValuePair("x_auth_mode", "client_auth"));
////        xauth_params.add(new BasicNameValuePair("x_auth_username", email));
////        xauth_params.add(new BasicNameValuePair("x_auth_password", password));
////        xauth_params.add(new BasicNameValuePair("oauth_verifier", oAuthToken.getOauthVerifier()));
//        HttpPost post = new HttpPost("http://www.tumblr.com/oauth/access_token");
//        try {
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(xauth_params);
//            post.setEntity(entity);
//            consumer.sign(post);
//            try {
//                response = client.processRequest(post);
//            } catch (TumblrException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//
//            System.out.println("asdfsad");
//
//        } catch (UnsupportedEncodingException e) {
//        }


//        HttpPost getAccessToken = new HttpPost("http://www.tumblr.com/oauth/access_token");
//        consumer.sign(getAccessToken);
//
//        try {
//            response = client.processRequest(getAccessToken);
//        } catch (TumblrException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


//        try {
//            client.processAccessToken(consumer,oAuthToken.getOauthVerifier());
//        } catch (TumblrException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

//        HttpPost write = new HttpPost("http://api.tumblr.com/v2/blog/" + blog + "/post?type=text&state=published&title=testJava&body=HoHoHoBo&api_key=" + oauth_key);
//
//        consumer.sign(write);
//
//        try {
//            response = client.processRequest(write);
//        } catch (TumblrException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

//        try {
//            client.processAccessToken(consumer, oauthToken.getOauthVerifier());
//        } catch (TumblrException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }


        ArrayList<BasicNameValuePair> xauth_params = new ArrayList<BasicNameValuePair>();
        xauth_params.add(new BasicNameValuePair("oauth_verifier", oauthToken.getOauthVerifier()));

//        String aa = "oauth_verifier=" + oauthToken.getOauthVerifier();

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(xauth_params);


        HttpPost post7 = new HttpPost("http://www.tumblr.com/oauth/access_token");
        post7.setEntity(entity);

//        signer.setConsumerSecret(oauth_secret);
        signer.setTokenSecret(oauthToken.getOauthTokenSecret());

        once = Long.toString(random.nextLong());
        timestamp = Long.toString(System.currentTimeMillis() / 1000L);

        httpParameters = new HttpParameters();
        httpParameters.put("oauth_consumer_key", oauth_key);
        httpParameters.put("oauth_nonce", once);
        httpParameters.put("oauth_signature_method", signer.getSignatureMethod());
        httpParameters.put("oauth_timestamp", timestamp);
        httpParameters.put("oauth_token", oauthToken.getOauthToken());
//        httpParameters.put("oauth_verifier", oauthToken.getOauthVerifier());
        httpParameters.put("oauth_version", "1.0");

        httpRequest = new HttpRequestAdapter(post7);

        sign = signer.sign(httpRequest, httpParameters);

//        httpParameters.remove("oauth_verifier");

        authHeaderValue = authorizationHeaderSigningStrategy.writeSignature(sign, httpRequest, httpParameters);

        post7.setHeader(new BasicHeader(OAuth.HTTP_AUTHORIZATION_HEADER, authHeaderValue));
        post7.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        post7.setHeader(new BasicHeader("Accept", "*/*"));

        try {
            response = client.processRequest(post7);
        } catch (TumblrException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//        HttpPost write = new HttpPost("http://api.tumblr.com/v2/blog/pereposter.tumblr.com/post?type=text&state=published&title=testJava&body=HoHoHoBo&api_key=s66LT5sOFgoFtbVw1ePkPjqssrYwYFfibWkE304xcOmVS2Upcv");
//
//        try {
//            response = client.processRequest(write);
//        } catch (TumblrException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        return null;
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

    private String getValueFromHtml(String nameParam, String body) {
        int beginIdx = body.indexOf(nameParam) + nameParam.length();
        int endIdx = body.substring(beginIdx).indexOf("\"");
        return body.substring(beginIdx, beginIdx + endIdx);
    }

}
