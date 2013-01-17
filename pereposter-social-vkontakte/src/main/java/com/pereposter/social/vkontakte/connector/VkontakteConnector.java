package com.pereposter.social.vkontakte.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.connector.SocialNetworkConnector;
import com.pereposter.social.entity.Post;
import com.pereposter.social.entity.Response;
import com.pereposter.social.entity.SocialAuth;
import com.pereposter.social.vkontakte.entity.*;
import org.apache.commons.lang3.StringEscapeUtils;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Value("${pereposter.social.vkontakte.url.write.post.wall}")
    private String writePostToUserWallUrl;

    @Value("${pereposter.social.vkontakte.url.get.postById}")
    private String getPostByIdUrl;

    @Value("${pereposter.social.vkontakte.url.get.postList}")
    private String getPostsFormWall;

    @Value("${pereposter.social.vkontakte.url.accessTokenParamName}")
    private String accessTokenParamName;

    private final String REMIXSID_PARAM_NAME = "remixsid";
    private final String accessTokenNameParam = "access_token=";

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

        String result = writePostToWall(auth, post);

        return Strings.isNullOrEmpty(result) ? null : result;
    }

    @Override
    public String writeNewPosts(SocialAuth auth, List<Post> posts) {

        WritePostResponse response = null;
        HttpPost httpPost;

        for (Post post : posts) {

            httpPost = new HttpPost(writePostToUserWallUrl + StringEscapeUtils.escapeHtml4(post.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth));

            String json = client.sendRequestReturnBody(httpPost);
            response = readJsonToObject(json, WritePostResponse.class);
        }

        String result = null;
        if (response != null && !Strings.isNullOrEmpty(response.getResponse().getPost_id()))
            result = response.getResponse().getPost_id();

        return result;

    }

    @Override
    public Post findPostById(SocialAuth auth, String postId) {

        HttpGet httpPost = new HttpGet(getPostByIdUrl + postId + accessTokenNameParam + getAccessToken(auth));

        String json = client.sendRequestReturnBody(httpPost);

        FindPostByIdResponse response = readJsonToObject(json, FindPostByIdResponse.class);

        Post result = null;

        if (response != null && response.getResponse().size() != 0) {
            result = createPost(response.getResponse().get(0));
        }

        return result;

    }

    @Override
    public List<Post> findPostsByOverCreatedDate(SocialAuth auth, DateTime createdDate) {

        //TODO: fix logic!!!!

        Integer count = 50;
        Integer offset = 0;

        List<Post> result = findPostOverCreateDate(auth, createdDate, count, offset);

        return result;
    }

    @Override
    public Post findLastPost(SocialAuth auth) {

        HttpGet httpGet = new HttpGet(getPostsFormWall + "?filter=owner&count=" + 1 + accessTokenParamName + getAccessToken(auth));
        String json = client.sendRequestReturnBody(httpGet);
        GetPostListResponse response = readJsonToObject(json, GetPostListResponse.class);

        Post result = null;

        if (response != null && response.getResponse().size() == 2) {

            PostVkontakte postVkontakte = createAndFillPostVkontakte((Map<String, Object>) response.getResponse().get(1));

            result = createPost(postVkontakte);
        }

        return result;
    }

    private PostVkontakte createAndFillPostVkontakte(Map<String, Object> map) {

        PostVkontakte result = new PostVkontakte();

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (entry.getKey().equals("id")) {
                result.setId(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("from_id")) {
                result.setDate(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("to_id")) {
                result.setTo_id(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("date")) {
                result.setDate(Long.parseLong(entry.getValue().toString()));
            }

            if (entry.getKey().equals("text")) {
                result.setText((String) entry.getValue());
            }


        }

        return result;

    }

    private List<Post> findPostOverCreateDate(SocialAuth auth, DateTime createdDate, Integer count, Integer offset) {
        HttpGet httpGet = new HttpGet(getPostsFormWall + "?filter=owner&count=" + count + "&offset=" + offset + accessTokenParamName + getAccessToken(auth));

        String json = client.sendRequestReturnBodyAndResponse(httpGet, true).getBody();

        GetPostListResponse response = readJsonToObject(json, GetPostListResponse.class);

        List<Post> result = new ArrayList<Post>();
        PostVkontakte postVkontakte;

        for (int i = 1; i < response.getResponse().size(); i++) {

            postVkontakte = createAndFillPostVkontakte((Map<String, Object>) response.getResponse().get(i));

            if (postVkontakte.getDate() > (createdDate.getMillis() / 1000)) {
                result.add(createPost(postVkontakte));
            } else {
                break;
            }

        }


        return result.isEmpty() ? null : result;
    }

    private String writePostToWall(SocialAuth auth, Post post) {
        HttpPost httpPost = new HttpPost(writePostToUserWallUrl + StringEscapeUtils.escapeHtml4(post.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth));

        String json = client.sendRequestReturnBody(httpPost);
        WritePostResponse response = readJsonToObject(json, WritePostResponse.class);

        return response.getResponse().getPost_id();
    }

    private Post createPost(PostVkontakte postVkontakte) {
        Post result = new Post();
        result.setCreatedDate(new DateTime(postVkontakte.getDate() * 1000));
        result.setId(postVkontakte.getId().toString());
        result.setMessage(postVkontakte.getText());
        return result;
    }

    private Post createPost(FindPostByIdResult findPostResponse) {
        Post result = new Post();
        result.setId(findPostResponse.getId().toString());
        result.setMessage(findPostResponse.getText());
        result.setCreatedDate(new DateTime(findPostResponse.getDate() * 1000));
        return result;
    }

    private <T> T readJsonToObject(String json, Class<T> clazz) {

        T result = null;
        try {
            result = objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            //TODO: пишем ошибку в лог
        }

        return result;

    }

    private String getAccessToken(SocialAuth auth) {

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

        response = client.sendRequestReturnBodyAndResponse(new HttpPost(getUrlByLocationHeader(response.getHttpResponse())), clearCookie);

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

    private Response step4(boolean clearCookie, Response response, CookieParam cookieParam) {
        String remixsid = gettingRemixsidFromCookie(response.getHttpResponse());

        HttpPost post = new HttpPost(getUrlByLocationHeader(response.getHttpResponse()));
        post.setHeaders(fillCookieClient(cookieParam, remixsid));

        response = client.sendRequestReturnBodyAndResponse(post, clearCookie);

        if (response.getHttpResponse().getStatusLine().getStatusCode() != 302) {
            //TODO: Пишем в лог
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }
        return response;
    }

    private Response step2(SocialAuth auth, boolean clearCookie, Response response) {
        ParamLoginForm paramLoginForm = gettingParamFormLoginForm(response.getBody());

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
        return response;
    }

    private Response step1(boolean clearCookie) {
        String authorizeUrlParams = authorizeUrl + client_id
                + "&scope=" + scope
                + "&redirect_uri=" + redirect_uri
                + "&display=" + display
                + "&response_type=" + response_type
                + "&_hash=0";

        return client.sendRequestReturnBodyAndResponse(new HttpGet(authorizeUrlParams), clearCookie);
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
        HttpResponse result;

        int begin = body.indexOf("<form method=\"post\" action=");
        int end = body.substring(begin + 28).indexOf("\">");

        String link = body.substring(begin + 28, begin + 28 + end);

        result = client.sendRequestReturnHttpResponse(new HttpPost(link));
        result = client.sendRequestReturnHttpResponse(new HttpPost(getUrlByLocationHeader(result)));

        return result;
    }

    private String getUrlByLocationHeader(HttpResponse httpResponse) {
        return httpResponse.getFirstHeader("location").getValue();
    }
}
