package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.connector.SocialNetworkConnector;
import com.pereposter.social.entity.Post;
import com.pereposter.social.entity.Response;
import com.pereposter.social.entity.SocialAuth;
import com.pereposter.social.vkontakte.entity.CookieParam;
import com.pereposter.social.vkontakte.entity.ParamLoginForm;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VkontakteConnector implements SocialNetworkConnector {

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
    private final String accessTokenNameParam = "access_token=";

    private final String ORIGIN_FIND_STRING = "name=\"_origin\" value=\"";
    private final Integer ORIGIN_LENGTH = 22;

    private final String IP_H_FIND_STRING = "name=\"ip_h\" value=\"";
    private final Integer IP_H_LENGTH = 19;

    private final String TO_FIND_STRING = "name=\"to\" value=\"";
    private final Integer TO_LENGTH = 17;

    private final String ACTION_URL_FIND_STRING = "<form method=\"post\" action=\"";
    private final Integer ACTION_URL_LENGTH = 28;

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String writeNewPost(SocialAuth auth, Post post) {

        getAccessToken(auth);

        return null;
    }

    @Override
    public String writeNewPosts(SocialAuth auth, List<Post> posts) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Post findPostById(SocialAuth auth, String postId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Post> findPostsByOverCreatedDate(SocialAuth auth, DateTime createdDate) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Post findLastPost(SocialAuth auth) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getAccessToken(SocialAuth auth) {

        boolean clearCookie = true;

        String authorizeUrlParams = authorizeUrl + client_id
                + "&scope=" + scope
                + "&redirect_uri=" + redirect_uri
                + "&display=" + display
                + "&response_type=" + response_type
                + "&_hash=0";

        Response response = client.sendRequestReturnBodyAndResponse(new HttpGet(authorizeUrlParams), clearCookie);

        clearCookie = false;

        ParamLoginForm paramLoginForm = gettingParamFormLoginForm(response.getBody());

        assert !Strings.isNullOrEmpty(paramLoginForm.getActionUrlParam());
        assert !Strings.isNullOrEmpty(paramLoginForm.getIpHParam());
        assert !Strings.isNullOrEmpty(paramLoginForm.getToParam());

        /////////////////////////////////
        //      STEP 2
        /////////////////////////////////

        String loginUrl = paramLoginForm.getActionUrlParam() +
                "&ip_h=" + paramLoginForm.getIpHParam() +
                "&to=" + paramLoginForm.getToParam() +
                "&email=" + auth.getLogin() +
                "&pass=" + auth.getPassword();

        response = client.sendRequestReturnBodyAndResponse(new HttpPost(loginUrl), clearCookie);

        if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: писать в лог
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }

        CookieParam cookieParam = getLoginHashAndPasswordHashFromCookie(response.getHttpResponse().getHeaders("set-cookie"));

        assert Strings.isNullOrEmpty(cookieParam.getLoginParam());
        assert Strings.isNullOrEmpty(cookieParam.getPassParam());

        /////////////////////////////////
        //      STEP 3
        /////////////////////////////////

        response = client.sendRequestReturnBodyAndResponse(new HttpPost(getUrlByLocationHeader(response.getHttpResponse())),clearCookie);

        //Окно одобрения приложения соц сети
        if (response.getHttpResponse().getStatusLine().getStatusCode() == 200) {
            response.setHttpResponse(confirmationApplication(response.getBody()));
        } else if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: Пишем в лог
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }

        String remixsid = gettingRemixsidFromCookie(response.getHttpResponse());
        assert remixsid != null;

        /////////////////////////////////
        //      STEP 4
        /////////////////////////////////

        HttpPost post = new HttpPost(getUrlByLocationHeader(response.getHttpResponse()));
        post.setHeaders(fillCookieClient(cookieParam, remixsid));

        response = client.sendRequestReturnBodyAndResponse(post, clearCookie);

        if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: Пишем в лог
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }

        String link = getUrlByLocationHeader(response.getHttpResponse());

        return gettingAccessTokenFromUrl(link);
    }

    private ParamLoginForm gettingParamFormLoginForm(String body) {
        //        int originBeginIndex = body.indexOf(ORIGIN_FIND_STRING) + ORIGIN_LENGTH;
//        int originBeginEnd = originBeginIndex + body.substring(originBeginIndex).indexOf("\"");
//        String originParam = body.substring(originBeginIndex, originBeginEnd);

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

    private String gettingAccessTokenFromUrl(String link) {

        int begin = link.indexOf(accessTokenNameParam);
        int end = link.substring(begin + accessTokenNameParam.length()).indexOf("&");

        String accessToken = null;
        if (end != -1) {
            accessToken = link.substring(begin + accessTokenNameParam.length(), end + begin + accessTokenNameParam.length());
        } else {
            accessToken = link.substring(begin + accessTokenNameParam.length());
        }
        return accessToken;
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

    private HttpResponse confirmationApplication(String body) {
        HttpResponse httpResponse;
        int aaa = body.indexOf("<form method=\"post\" action=");

        int ccc = body.substring(aaa + 28).indexOf("\">");

        String link = body.substring(aaa + 28, aaa + 28 + ccc);

        httpResponse = client.sendRequestReturnHttpResponse(new HttpPost(link));

        httpResponse = client.sendRequestReturnHttpResponse(new HttpPost(getUrlByLocationHeader(httpResponse)));
        return httpResponse;
    }

    private String getUrlByLocationHeader(HttpResponse httpResponse) {
        return httpResponse.getFirstHeader("location").getValue();
    }
}
