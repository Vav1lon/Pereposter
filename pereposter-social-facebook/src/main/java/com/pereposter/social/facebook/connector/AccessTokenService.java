package com.pereposter.social.facebook.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.FacebookException;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.api.entity.SocialAuthEntity;
import com.pereposter.social.facebook.entity.AccessToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component("facebookAccessTokenService")
public class AccessTokenService {

    private final static Logger LOGGER = LoggerFactory.getLogger(FacebookConnector.class);

    private final String DART_COOKIE = "datr=";

    @Value("${pereposter.social.facebook.url.request.clientId}")
    private String clientId;

    @Value("${pereposter.social.facebook.url.request.accessToken}")
    private String urlAccessToken;

    @Value("${pereposter.social.facebook.url.request.redirectUri}")
    private String redirectUri;

    @Value("${pereposter.social.facebook.url.request.scopeRead}")
    private String scopeRead;

    @Value("${pereposter.social.facebook.url.request.scopeWrite}")
    private String scopeWrite;

    @Value("${pereposter.social.facebook.url.request.responseType}")
    private String responseType;

    private final String accesstokenFromUrlParamName = "access_token=";
    private final String expiresInFromUrlParamName = "expires_in=";

    @Autowired
    private Client client;

    public AccessToken getAccessToken(SocialAuthEntity auth) throws FacebookException {

        Response response;
        String body;
        HttpPost httpPost;

        // Step 1
        // return link for login form
        String mainUrl = urlAccessToken
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=" + createScopeString(scopeRead, scopeWrite)
                + "&response_type=" + responseType;

        response = client.processRequest(new HttpPost(mainUrl));

        if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: пишем в лог что логин и пароль не пршел
            String errorMessage = "Error, not valid facebook response > step 1";
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Step 2
        // Open form login or extracted accessToken from url
        String urlLoginPage = response.getHttpResponse().getFirstHeader("location").getValue();

        if (!urlLoginPage.contains(accesstokenFromUrlParamName)) {

            httpPost = new HttpPost(urlLoginPage);
            body = client.processRequest(httpPost).getBody();

            if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
                //TODO: пишем в лог что логин и пароль не пршел
                String errorMessage = "Error, not valid facebook response > step 2";
                LOGGER.error(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }

            // Step 3
            // Send login form
            httpPost = getHttpPost(auth.getLogin(), auth.getPassword(), response.getHttpResponse(), body);
            response = client.processRequest(httpPost);

            if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
                //TODO: пишем в лог что логин и пароль не пршел
                String errorMessage = "Error, not valid facebook response > step 3";
                LOGGER.error(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }


            // Step 4
            // get accessToken url
            httpPost = new HttpPost(mainUrl);
            response = client.processRequest(httpPost);


            //Step 4_1 solve app
            if (response.getHttpResponse().getFirstHeader("location") == null) {

                String formContent = response.getBody();

                String fb_dtsgParamName = "name=\"fb_dtsg\" value=\"";
                String new_permsParamName = "name=\"new_perms\" value=\"";
                String orig_permsParamName = "name=\"orig_perms\" value=\"";
                String dubstepParamName = "name=\"dubstep\" value=\"";
                String _pathParamName = "name=\"_path\" value=\"";
                String app_idParamName = "name=\"app_id\" value=\"";
                String redirect_uriParamName = "name=\"redirect_uri\" value=\"";
                String displayParamName = "name=\"display\" value=\"";
                String response_typeParamName = "name=\"response_type\" value=\"";
                String fbconnectParamName = "name=\"fbconnect\" value=\"";
                String from_postParamName = "name=\"from_post\" value=\"";
                String urlParamName = "form id=\"uiserver_form\" action=\"";

                String permsParamValue = createScopeString(scopeWrite, scopeRead);
                String fb_dtsgParamValue = getValueFormContent(formContent, fb_dtsgParamName);
                String new_permsParamValue = getValueFormContent(formContent, new_permsParamName);
                String orig_permsParamValue = getValueFormContent(formContent, orig_permsParamName);
                String dubstepParamValue = getValueFormContent(formContent, dubstepParamName);
                String _pathParamValue = getValueFormContent(formContent, _pathParamName);
                String app_idParamValue = getValueFormContent(formContent, app_idParamName);
                String redirect_uriParamValue = getValueFormContent(formContent, redirect_uriParamName);
                String displayParamValue = getValueFormContent(formContent, displayParamName);
                String response_typeParamValue = getValueFormContent(formContent, response_typeParamName);
                String fbconnectParamValue = getValueFormContent(formContent, fbconnectParamName);
                String from_postParamValue = getValueFormContent(formContent, from_postParamName);
                String urlParamValue = getValueFormContent(formContent, urlParamName);

                List<NameValuePair> httpEntities = fillHttpEntities(permsParamValue, fb_dtsgParamValue, new_permsParamValue, orig_permsParamValue, dubstepParamValue, _pathParamValue, app_idParamValue, redirect_uriParamValue, displayParamValue, response_typeParamValue, fbconnectParamValue, from_postParamValue);

                httpPost = setHttpEntityToHttpPost(urlParamValue, httpEntities);

                response = client.processRequest(httpPost);
            }

        }

        return fillAccessTokenFromUrl(response.getHttpResponse().getFirstHeader("location").getValue());
    }

    private HttpPost setHttpEntityToHttpPost(String url, List<NameValuePair> httpEntities) {

        HttpPost result = new HttpPost(url);

        try {
            result.setEntity(new UrlEncodedFormEntity(httpEntities, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<NameValuePair> fillHttpEntities(String permsParamValue, String fb_dtsgParamValue, String new_permsParamValue, String orig_permsParamValue, String dubstepParamValue, String _pathParamValue, String app_idParamValue, String redirect_uriParamValue, String displayParamValue, String response_typeParamValue, String fbconnectParamValue, String from_postParamValue) {
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        result.add(new BasicNameValuePair("perms", permsParamValue));
        result.add(new BasicNameValuePair("fb_dtsg", fb_dtsgParamValue));
        result.add(new BasicNameValuePair("new_perms", new_permsParamValue));
        result.add(new BasicNameValuePair("orig_perms", orig_permsParamValue));
        result.add(new BasicNameValuePair("dubstep", dubstepParamValue));
        result.add(new BasicNameValuePair("_path", _pathParamValue));
        result.add(new BasicNameValuePair("app_id", app_idParamValue));
        result.add(new BasicNameValuePair("redirect_uri", redirect_uriParamValue));
        result.add(new BasicNameValuePair("display", displayParamValue));
        result.add(new BasicNameValuePair("response_type", response_typeParamValue));
        result.add(new BasicNameValuePair("fbconnect", fbconnectParamValue));
        result.add(new BasicNameValuePair("from_post", from_postParamValue));
        result.add(new BasicNameValuePair("opt_out_perms", ""));
        result.add(new BasicNameValuePair("grant_clicked", "solve"));
        return result;
    }

    private String createScopeString(String... scops) {
        return StringUtils.join(scops, ",");
    }

    private String getValueFormContent(String formContent, String findParam) {
        int begin = formContent.indexOf(findParam);
        int end = formContent.substring(begin + findParam.length()).indexOf("\"");
        return formContent.substring(begin + findParam.length(), begin + end + findParam.length());
    }

    private String getParamValueFromUrl(String paramName, String url) {
        int begin = url.indexOf(paramName);
        int end = url.substring(begin + paramName.length()).indexOf("&");

        String result = null;
        if (begin != -1) {
            if (end != -1) {
                result = url.substring(begin + paramName.length(), end + begin + paramName.length());
            } else {
                result = url.substring(begin + paramName.length());
            }
        }
        return result;
    }

    private AccessToken fillAccessTokenFromUrl(String url) {
        String accessToken = getParamValueFromUrl(accesstokenFromUrlParamName, url);
        String expiresIn = getParamValueFromUrl(expiresInFromUrlParamName, url);

        AccessToken result = null;

        if (!Strings.isNullOrEmpty(accessToken) && !Strings.isNullOrEmpty(expiresIn)) {
            result = new AccessToken();
            result.setAccessToken(accessToken);
            result.setExpiresIn(Long.parseLong(expiresIn));
        }

        return result;
    }

    private HttpPost getHttpPost(String login, String password, HttpResponse httpResponse, String body) {
        HttpPost httpPost;
        int being = body.indexOf("<form id=\"login_form\" action=\"");
        int end = body.substring(being + 30).indexOf("\"");
        String urlLoginForm = body.substring(being + 30, being + 30 + end);

        being = body.indexOf("name=\"lsd\" value=\"");
        end = body.substring(being + 18).indexOf("\"");
        String lsdParam = body.substring(being + 18, being + 18 + end);

        httpPost = setHttpEntityToHttpPost(urlLoginForm, fillFormParams(login, password, lsdParam));
        httpPost.setHeader(readAndCreateCookie(httpResponse));
        return httpPost;
    }

    private List<NameValuePair> fillFormParams(String login, String password, String lsdParam) {
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        result.add(new BasicNameValuePair("email", login));
        result.add(new BasicNameValuePair("pass", password));
        result.add(new BasicNameValuePair("api_key", clientId));
        result.add(new BasicNameValuePair("lsd", lsdParam));
        return result;
    }

    private Header readAndCreateCookie(HttpResponse httpResponse) {
        Header[] headers = httpResponse.getHeaders("set-cookie");

        String datrCookie = null;

        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];

            String cookie = header.getValue();

            if (cookie.contains(DART_COOKIE)) {
                datrCookie = cookie.substring(cookie.indexOf(DART_COOKIE) + DART_COOKIE.length(), cookie.indexOf(DART_COOKIE) + DART_COOKIE.length() + cookie.substring(cookie.indexOf(DART_COOKIE) + DART_COOKIE.length()).indexOf(";"));
            }
        }

        return new BasicHeader("Cookie", DART_COOKIE + datrCookie);
    }

}
