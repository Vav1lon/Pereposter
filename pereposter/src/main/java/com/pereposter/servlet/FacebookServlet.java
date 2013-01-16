package com.pereposter.servlet;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacebookServlet extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        String bodyResponse = null;

        /////////////////////////////////
        //      STEP 1
        /////////////////////////////////
        String url = "https://www.facebook.com/dialog/oauth/?client_id=255541331239101&redirect_uri=http://pereposter.com:8080/auth/facebook&scope=email,publish_stream&response_type=token";

        HttpPost firstHttpPost = new HttpPost(url);
        try {
            httpResponse = httpClient.execute(firstHttpPost);
            bodyResponse = IOUtils.toString(((BasicHttpResponse) httpResponse).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        firstHttpPost.abort();


        /////////////////////////////////
        //      STEP 2
        /////////////////////////////////
        String secondurl = httpResponse.getFirstHeader("location").getValue();

        HttpPost secondHttpPost = new HttpPost(secondurl);
        httpResponse = null;
        bodyResponse = null;
        httpClient = new DefaultHttpClient();
        try {
            httpResponse = httpClient.execute(secondHttpPost);
            bodyResponse = IOUtils.toString(((BasicHttpResponse) httpResponse).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        secondHttpPost.abort();


        /////////////////////////////////
        //      STEP 3
        /////////////////////////////////

        int being = bodyResponse.indexOf("<form id=\"login_form\" action=\"");
        int end = bodyResponse.substring(being + 30).indexOf("\"");
        String url3 = bodyResponse.substring(being + 30, being + 30 + end);

        being = bodyResponse.indexOf("name=\"lsd\" value=\"");
        end = bodyResponse.substring(being + 18).indexOf("\"");
        String lsdParam = bodyResponse.substring(being + 18, being + 18 + end);

        HttpPost httpPost3 = new HttpPost(url3);

        Header[] headers = httpResponse.getHeaders("set-cookie");

        String datrCoo = null;

        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];

            String cookie = header.getValue();

            if (cookie.indexOf("datr=") != -1) {
                datrCoo = cookie.substring(cookie.indexOf("datr") + 5, cookie.indexOf("datr") + 5 + cookie.substring(cookie.indexOf("datr") + 5).indexOf(";"));
            }

        }

        List<BasicHeader> basicHeaders = new ArrayList<BasicHeader>();
        basicHeaders.add(new BasicHeader("Cookie", "datr=" + datrCoo));
        httpPost3.setHeaders(basicHeaders.toArray(new BasicHeader[basicHeaders.size()]));

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("email", "denis.kuzmin.7758@facebook.com"));
        nvps.add(new BasicNameValuePair("pass", "A329k4219516811"));
        nvps.add(new BasicNameValuePair("api_key", "255541331239101"));
        nvps.add(new BasicNameValuePair("lsd", lsdParam));
        httpPost3.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

        httpResponse = null;
        bodyResponse = null;
        httpClient = new DefaultHttpClient();
        try {
            httpResponse = httpClient.execute(httpPost3);
            bodyResponse = IOUtils.toString(((BasicHttpResponse) httpResponse).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        httpPost3.abort();

        if (httpResponse.getStatusLine().getStatusCode() != 302) {
            throw new IllegalArgumentException("Херня пришла!");
        }

        /////////////////////////////////
        //      STEP 4
        /////////////////////////////////

        HttpPost httpPost4 = new HttpPost(url);

        httpResponse = null;
        bodyResponse = null;

        try {
            httpResponse = httpClient.execute(httpPost4);
            bodyResponse = IOUtils.toString(((BasicHttpResponse) httpResponse).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        httpPost4.abort();

        /////////////////////////////////
        //      STEP 5
        /////////////////////////////////
        //Location: http://pereposter.com:8080/auth/facebook#access_token=AAADoadpGIL0BADotJHKNlMybQFEnZBZCF1ZCx3nPjagu6qiDtJM6irC6kPRPU0Iuw85atleLviCfRA1IlbveneqWctceIg8z0ZCzbo2D59Dhv96JGTX2&expires_in=6141
        String urlWithAccessToken = httpResponse.getFirstHeader("location").getValue();

        being = urlWithAccessToken.indexOf("#access_token=");
        end = urlWithAccessToken.substring(urlWithAccessToken.indexOf("#access_token=") + 14).indexOf("&");

        //SELECT app_data, post_id, attachment, actor_id, target_id, message, type, is_hidden, updated_time, created_time FROM stream WHERE source_id = me() AND (type = 46 OR type = 247 OR type = 80) AND is_hidden = 0 LIMIT 1
        //SELECT app_data, post_id, attachment, actor_id, target_id, message, type, is_hidden, updated_time, created_time FROM stream WHERE source_id = me() AND (type = 46 OR type = 247 OR type = 80) AND is_hidden = 0 AND created_time < 1358172137
        //SELECT height, photo_id, size, src, width FROM photo_src WHERE photo_id in (118458438326758)

        //TODO: Сделать проверку на послений параметер

        String accessToken = urlWithAccessToken.substring(being + 14, being + 14 + end);


        HttpPost post = new HttpPost("https://graph.facebook.com/100004878072152/feed?message=01.01.2012%20Сергей%20попросил%20написать%20пост%20%20%20Проверка%20того%20как%20будут%20постится%20большие%20почты%20%20%20как%20то%20так&access_token=" + accessToken);
        try {
            httpResponse = httpClient.execute(post);
            bodyResponse = IOUtils.toString(((BasicHttpResponse) httpResponse).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        post.abort();

    }
}
