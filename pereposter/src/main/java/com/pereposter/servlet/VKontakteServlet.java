package com.pereposter.servlet;

import org.apache.cxf.helpers.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VKontakteServlet extends HttpServlet {

    private final int ORIGIN_LENGTH = 22;
    private final String ORIGIN_FIND_STRING = "name=\"_origin\" value=\"";

    private final int IP_H_LENGTH = 19;
    private final String IP_H_FIND_STRING = "name=\"ip_h\" value=\"";

    private final int TO_LENGTH = 17;
    private final String TO_FIND_STRING = "name=\"to\" value=\"";

    private final int ACTION_URL_LENGTH = 28;
    private final String ACTION_URL_FIND_STRING = "<form method=\"post\" action=\"";


    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /////////////////////////////////
        //      STEP 1
        /////////////////////////////////


        Integer client_id = 3275375;
        String scope = "offline,wall";
        String redirect_uri = "http://oauth.vk.com/blank.html";
        String display = "wap";
        String response_type = "token";

        String url = "http://oauth.vk.com/oauth/authorize?client_id=" + client_id + "&scope=" + scope + "&redirect_uri=" + redirect_uri + "&display=" + display + "&response_type=" + response_type + "&_hash=0";

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet post = new HttpGet(url);

        HttpResponse response1 = null;
        String body = null;
        try {
            response1 = httpClient.execute(post);
            body = IOUtils.toString(((BasicHttpResponse) response1).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        post.abort();

        assert 1 != 1;
        assert body != null;

        int originBeginIndex = body.indexOf(ORIGIN_FIND_STRING) + ORIGIN_LENGTH;
        int originBeginEnd = originBeginIndex + body.substring(originBeginIndex).indexOf("\"");
        String originParam = body.substring(originBeginIndex, originBeginEnd);

        int ipHBeginIndex = body.indexOf(IP_H_FIND_STRING) + IP_H_LENGTH;
        int ipHBeginEnd = ipHBeginIndex + body.substring(ipHBeginIndex).indexOf("\"");
        String ipHParam = body.substring(ipHBeginIndex, ipHBeginEnd);

        int toBeginIndex = body.indexOf(TO_FIND_STRING) + TO_LENGTH;
        int toBeginEnd = toBeginIndex + body.substring(toBeginIndex).indexOf("\"");
        String toParam = body.substring(toBeginIndex, toBeginEnd);

        int actionUrlBeginIndex = body.indexOf(ACTION_URL_FIND_STRING) + ACTION_URL_LENGTH;
        int actionUrlBeginEnd = actionUrlBeginIndex + body.substring(actionUrlBeginIndex).indexOf("\"");
        String actionUrlParam = body.substring(actionUrlBeginIndex, actionUrlBeginEnd);

        /////////////////////////////////
        //      STEP 2
        /////////////////////////////////

        HttpPost httpPost = new HttpPost(actionUrlParam + "&ip_h=" + ipHParam + "&to=" + toParam + "&email=pereposter@lenta.ru&pass=19516811");
        response1 = null;
        try {
            response1 = httpClient.execute(httpPost);
            body = IOUtils.toString(((BasicHttpResponse) response1).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        httpPost.abort();

        if (response1.getStatusLine().getStatusCode() != 302) {
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }

        assert response1.getStatusLine().getStatusCode() == 200;
        assert response1.getHeaders("Set-Cookie") != null;


        Header[] a = response1.getHeaders("set-cookie");
        Header test = null;
        String loginParam = null;
        String passParam = null;
        for (int i = 0; i < a.length; i++) {
            test = a[i];

            int tmp = test.getValue().indexOf("l=");
            int tmp2 = test.getValue().indexOf("p=");

            if (tmp != -1) {

                int c = test.getValue().substring(tmp + 2).indexOf(";");
                loginParam = test.getValue().substring(tmp + 2, c);

            }
            if (tmp2 != -1) {
                int c = test.getValue().substring(tmp2 + 2).indexOf(";");
                passParam = test.getValue().substring(tmp2 + 2, c);
            }
        }

        assert loginParam != null;
        assert passParam != null;

        /////////////////////////////////
        //      STEP 3
        /////////////////////////////////

        String cc = response1.getFirstHeader("location").getValue();

        HttpPost httpGet = new HttpPost(cc);
        response1 = null;
        try {
            response1 = httpClient.execute(httpGet);
            body = IOUtils.toString(((BasicHttpResponse) response1).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        httpGet.abort();

        if (response1.getStatusLine().getStatusCode() == 200) {

            int aaa = body.indexOf("<form method=\"post\" action=");

            int ccc = body.substring(aaa + 28).indexOf("\">");

            String link = body.substring(aaa + 28, aaa + 28 + ccc);

            HttpPost httpGet1 = new HttpPost(link);
            response1 = null;
            try {
                response1 = httpClient.execute(httpGet1);
                body = IOUtils.toString(((BasicHttpResponse) response1).getEntity().getContent(), "UTF-8");
            } catch (Exception e) {

            }
            httpGet1.abort();

            String cc1 = response1.getFirstHeader("location").getValue();

            HttpPost httpGet2 = new HttpPost(cc1);
            response1 = null;
            try {
                response1 = httpClient.execute(httpGet2);
                body = IOUtils.toString(((BasicHttpResponse) response1).getEntity().getContent(), "UTF-8");
            } catch (Exception e) {

            }
            httpGet2.abort();


        } else if (response1.getStatusLine().getStatusCode() != 302) {
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }

        a = response1.getHeaders("set-cookie");
        String remixsid = null;
        for (int i = 0; i < a.length; i++) {
            test = a[i];

            int tmp = test.getValue().indexOf("remixsid");

            if (tmp != -1) {
                int c = test.getValue().substring(tmp + 9).indexOf(";");
                remixsid = test.getValue().substring(tmp + 9, c);
            }

        }

        assert remixsid != null;

        /////////////////////////////////
        //      STEP 4
        /////////////////////////////////

        HttpPost geet = new HttpPost(response1.getFirstHeader("location").getValue());

        List<BasicHeader> basicHeaders = new ArrayList<BasicHeader>();
        basicHeaders.add(new BasicHeader("Cookie", "remixsid=" + remixsid));
        basicHeaders.add(new BasicHeader("Cookie", "l=" + loginParam));
        basicHeaders.add(new BasicHeader("Cookie", "p=" + passParam));

        geet.setHeaders(basicHeaders.toArray(new BasicHeader[basicHeaders.size()]));

        response1 = null;
        try {
            response1 = httpClient.execute(geet);
        } catch (Exception e) {

        }
        geet.abort();

        if (response1.getStatusLine().getStatusCode() != 302) {
            throw new IllegalArgumentException("Не верный ответ пришел с сервера!");
        }


        //http://oauth.vk.com/blank.html#access_token=30ca1765f1ef76e27239c25c72409da1c503429c4e8c5fbfad3e5346e15a0f59340b1b14abf58453e0455&expires_in=0&user_id=195726384
        String link = response1.getFirstHeader("location").getValue();

        String nameKey = "access_token=";
        int begin = link.indexOf(nameKey);
        int end = link.substring(begin + nameKey.length()).indexOf("&");

        String accessToken = null;
        if (end != -1) {
            accessToken = link.substring(begin + nameKey.length(), end + begin + nameKey.length());
        } else {
            accessToken = link.substring(begin + nameKey.length());
        }

        String urll = "https://api.vk.com/method/wall.post?message=01.01.2012%20Сергей%20попросил%20написать%20пост%20%20%20Проверка%20того%20как%20будут%20постится%20большие%20почты%20%20%20как%20то%20так&out=0&access_token=" + accessToken;

        HttpPost lastRequest = new HttpPost(urll);

        response1 = null;
        try {
            response1 = httpClient.execute(lastRequest);
            body = IOUtils.toString(((BasicHttpResponse) response1).getEntity().getContent(), "UTF-8");
        } catch (Exception e) {

        }
        geet.abort();

        System.out.println(accessToken);

    }

}
