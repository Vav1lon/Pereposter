package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.VkontakteException;
import com.pereposter.social.api.entity.Response;
import com.pereposter.social.api.entity.SocialAuthEntity;
import com.pereposter.social.vkontakte.entity.AccessToken;
import com.pereposter.social.vkontakte.entity.CookieParam;
import com.pereposter.social.vkontakte.entity.ParamLoginForm;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("vkontakteAccessTokenService")
public class AccessTokenService {

    @Value("${pereposter.social.vkontakte.authorizeUrl}")
    private String authorizeUrl;

    @Value("${pereposter.social.vkontakte.client_id}")
    private long client_id;

    @Value("${pereposter.social.vkontakte.scope}")
    private String scope;

    @Value("${pereposter.social.vkontakte.redirect_uri}")
    private String redirect_uri;

    @Value("${pereposter.social.vkontakte.display}")
    private String display;

    @Value("${pereposter.social.vkontakte.response_type}")
    private String response_type;

    private final String REMIXSID_PARAM_NAME = "remixsid";
    private final String accesstokenFromUrlParamName = "access_token=";
    private final String expiresInFromUrlParamName = "expires_in=";
    private final String userIdFromUrlParamName = "user_id=";

    private final String IP_H_FIND_STRING = "name=\"ip_h\" value=\"";
    private final Integer IP_H_LENGTH = 19;

    private final String TO_FIND_STRING = "name=\"to\" value=\"";
    private final Integer TO_LENGTH = 17;

    private final String ACTION_URL_FIND_STRING = "<form method=\"post\" action=\"";
    private final Integer ACTION_URL_LENGTH = 28;

    @Autowired
    private Client client;

    public AccessToken getNewAccessToken(SocialAuthEntity auth) throws VkontakteException {

        boolean clearCookie = true;

        Response response = step1(clearCookie);

        clearCookie = false;

        /////////////////////////////////
        //      STEP 2
        /////////////////////////////////

        response = step2(auth, clearCookie, response);

        /////////////////////////////////
        //      STEP 3
        /////////////////////////////////

        CookieParam cookieParam = getLoginHashAndPasswordHashFromCookie(response.getHttpResponse().getHeaders("set-cookie"));

        response = client.processRequest(new HttpPost(getUrlByLocationHeader(response.getHttpResponse())), clearCookie);

        //Окно одобрения приложения соц сети
        if (response.getHttpResponse().getStatusLine().getStatusCode() == 200) {
            response.setHttpResponse(confirmationApplication(response.getBody()));
        } else if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: Пишем в лог
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }

        /////////////////////////////////
        //      STEP 4
        /////////////////////////////////

        response = step4(clearCookie, response, cookieParam);

        String link = getUrlByLocationHeader(response.getHttpResponse());

        return gettingAccessTokenFromUrl(link);
    }

    private Response step4(boolean clearCookie, Response response, CookieParam cookieParam) throws VkontakteException{
        String remixsid = gettingRemixsidFromCookie(response.getHttpResponse());

        HttpPost post = new HttpPost(getUrlByLocationHeader(response.getHttpResponse()));
        post.setHeaders(fillCookieClient(cookieParam, remixsid));

        response = client.processRequest(post, clearCookie);

        if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: Пишем в лог
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }
        return response;
    }

    private Response step2(SocialAuthEntity auth, boolean clearCookie, Response response)  throws VkontakteException {
        ParamLoginForm paramLoginForm = gettingParamFormLoginForm(response.getBody());

        String loginUrl = paramLoginForm.getActionUrlParam() +
                "&ip_h=" + paramLoginForm.getIpHParam() +
                "&to=" + paramLoginForm.getToParam() +
                "&email=" + auth.getLogin() +
                "&pass=" + auth.getPassword();

        response = client.processRequest(new HttpPost(loginUrl), clearCookie);

        if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: писать в лог
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }
        return response;
    }

    private Response step1(boolean clearCookie)  throws VkontakteException {
        String authorizeUrlParams = authorizeUrl + client_id
                + "&scope=" + scope
                + "&redirect_uri=" + redirect_uri
                + "&display=" + display
                + "&response_type=" + response_type
                + "&_hash=0";

        return client.processRequest(new HttpGet(authorizeUrlParams), clearCookie);
    }

    private Header[] fillCookieClient(CookieParam cookieParam, String remixsid) {
        List<BasicHeader> basicHeaders = new ArrayList<BasicHeader>();
        basicHeaders.add(new BasicHeader("Cookie", "remixsid=" + remixsid));
        basicHeaders.add(new BasicHeader("Cookie", "l=" + cookieParam.getLoginParam()));
        basicHeaders.add(new BasicHeader("Cookie", "p=" + cookieParam.getPassParam()));
        return basicHeaders.toArray(new BasicHeader[basicHeaders.size()]);
    }

    private String gettingRemixsidFromCookie(HttpResponse httpResponse) {

        String result = null;

        Header[] headers;
        Header header;
        headers = httpResponse.getHeaders("set-cookie");
        for (int i = 0; i < headers.length; i++) {
            header = headers[i];

            int cookieRow = header.getValue().indexOf(REMIXSID_PARAM_NAME);

            if (cookieRow != -1) {
                int c = header.getValue().substring(cookieRow + REMIXSID_PARAM_NAME.length() + 1).indexOf(";");
                result = header.getValue().substring(cookieRow + REMIXSID_PARAM_NAME.length() + 1, c);
            }

        }
        return result;
    }

    private HttpResponse confirmationApplication(String body)  throws VkontakteException {
        HttpResponse result;

        int begin = body.indexOf("<form method=\"post\" action=");
        int end = body.substring(begin + 28).indexOf("\">");

        String link = body.substring(begin + 28, begin + 28 + end);

        result = client.processRequest(new HttpPost(link), false).getHttpResponse();
        result = client.processRequest(new HttpPost(getUrlByLocationHeader(result)), false).getHttpResponse();

        return result;
    }

    private String getUrlByLocationHeader(HttpResponse httpResponse) {
        return httpResponse.getFirstHeader("location").getValue();
    }

    private AccessToken gettingAccessTokenFromUrl(String link) {

        String accessToekn = getParamValueFromUrl(accesstokenFromUrlParamName, link);
        String expiresIn = getParamValueFromUrl(expiresInFromUrlParamName, link);
        String userId = getParamValueFromUrl(userIdFromUrlParamName, link);

        AccessToken result = null;

        if (!Strings.isNullOrEmpty(accessToekn) && !Strings.isNullOrEmpty(expiresIn) && !Strings.isNullOrEmpty(userId)) {
            result = new AccessToken(accessToekn, Long.parseLong(expiresIn), userId);
        }
        return result;
    }

    private ParamLoginForm gettingParamFormLoginForm(String body) {

        int ipHBeginIndex = body.indexOf(IP_H_FIND_STRING) + IP_H_LENGTH;
        int ipHBeginEnd = ipHBeginIndex + body.substring(ipHBeginIndex).indexOf("\"");
        String ipHParam = body.substring(ipHBeginIndex, ipHBeginEnd);

        int toBeginIndex = body.indexOf(TO_FIND_STRING) + TO_LENGTH;
        int toBeginEnd = toBeginIndex + body.substring(toBeginIndex).indexOf("\"");
        String toParam = body.substring(toBeginIndex, toBeginEnd);

        int actionUrlBeginIndex = body.indexOf(ACTION_URL_FIND_STRING) + ACTION_URL_LENGTH;
        int actionUrlBeginEnd = actionUrlBeginIndex + body.substring(actionUrlBeginIndex).indexOf("\"");
        String actionUrlParam = body.substring(actionUrlBeginIndex, actionUrlBeginEnd);

        return new ParamLoginForm(ipHParam, toParam, actionUrlParam);

    }

    private CookieParam getLoginHashAndPasswordHashFromCookie(Header[] headers) {

        String loginParam = null;
        String passParam = null;
        Header header;
        for (int i = 0; i < headers.length; i++) {
            header = headers[i];

            int loginParamNum = header.getValue().indexOf("l=");
            if (loginParamNum != -1) {
                loginParam = getValueParamFormCookie(header, loginParamNum);
            }

            int passParamNum = header.getValue().indexOf("p=");
            if (passParamNum != -1) {
                passParam = getValueParamFormCookie(header, passParamNum);
            }

        }

        return new CookieParam(loginParam, passParam);
    }

    private String getValueParamFormCookie(Header header, int lnameParam) {
        int begin = header.getValue().substring(lnameParam + 2).indexOf(";");
        return header.getValue().substring(lnameParam + 2, lnameParam + 2 + begin);
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

}
