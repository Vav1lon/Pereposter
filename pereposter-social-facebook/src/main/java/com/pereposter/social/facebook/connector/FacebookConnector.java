package com.pereposter.social.facebook.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.connector.SocialNetworkConnector;
import com.pereposter.social.entity.Post;
import com.pereposter.social.entity.SocialAuth;
import com.pereposter.social.facebook.entity.PostFacebook;
import com.pereposter.social.facebook.entity.PostResponse;
import com.pereposter.social.facebook.entity.WritePostResult;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Component("facebookConnector")
public class FacebookConnector implements SocialNetworkConnector {

    private final String DART_COOKIE = "datr=";

    @Value("${pereposter.social.facebook.api_key}")
    private String apiKey;

    @Value("${pereposter.social.facebook.url.request.accessToken}")
    private String urlAccessToken;

    @Value("${pereposter.social.facebook.url.request.accessTokenParamName}")
    private String accessTokenParamName;

    @Value("${pereposter.social.facebook.fql.findLastPost}")
    private String fqlFindLastPost;

    @Value("${pereposter.social.facebook.fql.findPostById}")
    private String fqlFindPostById;

    @Value("${pereposter.social.facebook.fql.findPostsByOverCreatedDate}")
    private String fqlFindPostsByOverCreatedDate;

    @Value("${pereposter.social.facebook.graph.writePost}")
    private String graphWritePost;

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String writeNewPost(SocialAuth auth, Post post) {

        HttpPost httpPost = new HttpPost(graphWritePost + StringEscapeUtils.unescapeHtml4(post.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth));

        return readDataFromResponse(httpPost, WritePostResult.class).getId();
    }

    @Override
    public String writeNewPosts(SocialAuth auth, List<Post> posts) {

        HttpPost httpPost;
        String result = null;

        for (Post post : posts) {
            httpPost = new HttpPost(graphWritePost + StringEscapeUtils.unescapeHtml4(post.getMessage()).replace(" ", "%20") + accessTokenParamName + getAccessToken(auth));
            WritePostResult writePostResult = readDataFromResponse(httpPost, WritePostResult.class);
            if (writePostResult != null) {
                result = writePostResult.getId();
            }
        }


        return result;
    }

    @Override
    public Post findPostById(SocialAuth auth, String postId) {

        Post result = null;

        String buildUrl = fqlFindPostById + "\"" + postId + "\"" + accessTokenParamName + getAccessToken(auth);

        PostResponse response = readDataFromResponse(new HttpGet(buildUrl), PostResponse.class);

        if (response != null && !response.getData().isEmpty()) {
            result = createAndFillPostFromPostResponse(response);
        }

        return result;
    }

    @Override
    public List<Post> findPostsByOverCreatedDate(SocialAuth auth, DateTime createdDate) {

        List<Post> result = new ArrayList<Post>();

        PostResponse response = readDataFromResponse(new HttpGet(fqlFindPostsByOverCreatedDate + createdDate.getMillis() / 1000 + accessTokenParamName + getAccessToken(auth)), PostResponse.class);

        for (PostFacebook postFacebook : response.getData()) {
            result.add(createAndFillPostFromPostFacebook(postFacebook));
        }

        return result.isEmpty() ? null : result;
    }

    @Override
    public Post findLastPost(SocialAuth auth) {
        PostResponse result = readDataFromResponse(new HttpGet(fqlFindLastPost + accessTokenParamName + getAccessToken(auth)), PostResponse.class);
        return createAndFillPostFromPostResponse(result);
    }

    private Post createAndFillPostFromPostFacebook(PostFacebook post) {
        Post result = new Post();
        result.setId(post.getPost_id());
        result.setMessage(post.getMessage());
        result.setCreatedDate(new DateTime(post.getCreated_time() * 1000));
        result.setUpdatedDate(new DateTime(post.getUpdated_time() * 1000));
        return result;
    }

    private Post createAndFillPostFromPostResponse(PostResponse response) {

        Post result = null;

        if (response != null && !response.getData().isEmpty()) {
            PostFacebook postFacebook = response.getData().get(0);
            result = createAndFillPostFromPostFacebook(postFacebook);
        }

        return result;
    }

//    @Deprecated
//    public PostResponse findLastUserPost(String login, String password) {
//
//        HttpGet get = new HttpGet(fqlFindLastPost + accessTokenParamName + getAccessToken(login, password));
//
//        PostResponse result = readDataToPostResponse(get);
//
//        return result;
//    }
//
//    @Deprecated
//    public PostFacebook findPostById(String login, String password, String postId) {
//
//        HttpGet get = new HttpGet(fqlFindPostById + postId + accessTokenParamName + getAccessToken(login, password));
//
//        PostResponse result = readDataToPostResponse(get);
//
//        return result != null ? result.getData().isEmpty() ? null : result.getData().get(0) : null;
//
//    }
//
//    @Deprecated
//    public List<PostFacebook> findPostByOverCreateDate(String login, String password, DateTime createdDate) {
//
//        HttpGet get = new HttpGet(fqlFindPostsByOverCreatedDate + createdDate.getMillis() + accessTokenParamName + getAccessToken(login, password));
//
//        PostResponse result = readDataToPostResponse(get);
//
//        return result != null ? result.getData().isEmpty() ? null : result.getData() : null;
//    }
//
//    @Deprecated
//    public void writePost(String login, String password, String post) {
//
//        HttpPost httpPost = new HttpPost(graphWritePost + StringEscapeUtils.unescapeHtml4(post).replace(" ", "%20") + accessTokenParamName + getAccessToken(login, password));
//
//        WritePostResult result = readDataFromResponse(httpPost, WritePostResult.class);
//
//        System.out.println(result.toString());
//
//
//    }
//
//    @Deprecated
//    private PostResponse readDataToPostResponse(HttpGet get) {
//        String json = client.sendRequestReturnBody(get);
//
//        PostResponse result = null;
//        if (!Strings.isNullOrEmpty(json)) {
//            try {
//                result = objectMapper.readValue(json, PostResponse.class);
//            } catch (IOException e) {
//                //TODO: писать в лог онибку!
//                e.printStackTrace();
//            }
//
//        }
//        return result;
//    }

    private <T> T readDataFromResponse(HttpUriRequest query, Class<T> clazz) {
        String json = client.sendRequestReturnBody(query);

        T result = null;
        if (!Strings.isNullOrEmpty(json)) {
            try {
                result = objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                //TODO: писать в лог онибку!
                e.printStackTrace();
            }

        }
        return result;
    }

    private String getAccessToken(SocialAuth auth) {

        HttpResponse httpResponse = null;
        String body = null;
        HttpPost httpPost = null;

        // Step 1
        httpPost = new HttpPost(urlAccessToken);
        httpResponse = client.sendRequestReturnHttpResponse(httpPost);

        if (httpResponse.getStatusLine().getStatusCode() != 302) {
            //TODO: пишем в лог что логин и пароль не пршел
            throw new IllegalArgumentException("Херня пришла!");
        }

        // Step 2
        String urlLoginPage = httpResponse.getFirstHeader("location").getValue();
        httpPost = new HttpPost(urlLoginPage);
        body = client.sendRequestReturnBody(httpPost);

        if (httpResponse.getStatusLine().getStatusCode() != 302) {
            //TODO: пишем в лог что логин и пароль не пршел
            throw new IllegalArgumentException("Херня пришла!");
        }

        // Step 3
        //TODO: убрать хардкод access_token
        if (!httpResponse.getFirstHeader("location").getValue().contains("access_token")) {
            httpPost = getHttpPost(auth.getLogin(), auth.getPassword(), httpResponse, body);
            httpResponse = client.sendRequestReturnHttpResponse(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() != 302) {
                //TODO: пишем в лог что логин и пароль не пршел
                throw new IllegalArgumentException("Херня пришла!");
            }
        }

        // Step 4
        httpPost = new HttpPost(urlAccessToken);
        httpResponse = client.sendRequestReturnHttpResponse(httpPost);

        return cutAccessToken(httpResponse);

    }

    private String cutAccessToken(HttpResponse httpResponse) {
        String urlWithAccessToken = httpResponse.getFirstHeader("location").getValue();

        int being = urlWithAccessToken.indexOf("#access_token=");
        int end = urlWithAccessToken.substring(urlWithAccessToken.indexOf("#access_token=") + 14).indexOf("&");

        //TODO: Сделать проверку на послений параметер
        return urlWithAccessToken.substring(being + 14, being + 14 + end);
    }

    private HttpPost getHttpPost(String login, String password, HttpResponse httpResponse, String body) {
        HttpPost httpPost;
        int being = body.indexOf("<form id=\"login_form\" action=\"");
        int end = body.substring(being + 30).indexOf("\"");
        String urlLoginForm = body.substring(being + 30, being + 30 + end);

        being = body.indexOf("name=\"lsd\" value=\"");
        end = body.substring(being + 18).indexOf("\"");
        String lsdParam = body.substring(being + 18, being + 18 + end);

        httpPost = new HttpPost(urlLoginForm);
        httpPost.setHeader(readAndCreateCookie(httpResponse));
        List<NameValuePair> nvps = fillFormParams(login, password, lsdParam);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpPost;
    }

    private List<NameValuePair> fillFormParams(String login, String password, String lsdParam) {
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        result.add(new BasicNameValuePair("email", login));
        result.add(new BasicNameValuePair("pass", password));
        result.add(new BasicNameValuePair("api_key", apiKey));
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
