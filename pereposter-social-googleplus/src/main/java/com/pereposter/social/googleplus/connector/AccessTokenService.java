package com.pereposter.social.googleplus.connector;

import com.google.common.base.Strings;
import com.pereposter.social.api.GooglePlusException;
import com.pereposter.social.api.entity.Response;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenService {

    @Autowired
    private Client client;

    public void getAccessToken() throws GooglePlusException {

        //Step 1 :: Login page

        HttpGet http = new HttpGet("https://accounts.google.com/ServiceLogin?service=oz&continue=https://plus.google.com/");

        Response response = null;

        try {
            response = client.processRequest(http);
        } catch (GooglePlusException e) {
            //TODO: пишем в лог
            e.printStackTrace();
        }


        // Step 2 :: Parse html and send login data

        if (response == null || Strings.isNullOrEmpty(response.getBody())) {
            //TODO: пишем в лог
            throw new GooglePlusException("Нет даннх о странице логина");
        }

        String fromUrlNameParam = "action=\"";

//        <form novalidate id="gaia_loginform" action="https://accounts.google.com/ServiceLoginAuth" method="post">
//        <input type="hidden" name="continue" id="continue" value="https://plus.google.com/" >
//        <input type="hidden" name="service" id="service" value="oz" >
//        <input type="hidden" name="dsh" id="dsh" value="-5959542678910488218" >
//        <input type="hidden" name="GALX" value="iUsVDrWM_-s">
//        <input type="hidden" name="timeStmp" id="timeStmp" value=''/>
//        <input type="hidden" name="secTok" id="secTok" value=''/>
//        <input type="hidden" id="_utf8" name="_utf8" value="&#9731;"/>
//        <input type="hidden" name="bgresponse" id="bgresponse" value="js_disabled">
//        <input type="email" spellcheck="false" name="Email" id="Email" value="" >
//        <input type="password" name="Passwd" id="Passwd" >
//        <input type="submit" class="g-button g-button-submit" name="signIn" id="signIn" value="Sign in">
//        <input type="checkbox"  name="PersistentCookie" id="PersistentCookie" value="yes"     checked="checked"  >
//        <input type="hidden" name="rmShown" value="1">
//        </form>

        String continueNameParam = "continue\" value=\"";
        String serviceNameParam = "service\" value=\"";
        String dshNameParam = "dsh\" value=\"";
//        String hlNameParam = "hl\" value=\"";
        String GALXNameParam = "GALX\"\n" +
                "         value=\"";
//        String timeStmpNameParam = "timeStmp\" value='";
//        String secTokNameParam = "secTok\" value='";
        String _utf8NameParam = "_utf8\" value=\"";
        String bgresponseNameParam = "bgresponse\" value=\"";
        String rmShownNameParam = "rmShown\" value=\"";


        String continueValueParam = readHtmlAndFindValueParam(continueNameParam, response.getBody());
        String serviceValueParam = readHtmlAndFindValueParam(serviceNameParam, response.getBody());
        String dshValueParam = readHtmlAndFindValueParam(dshNameParam, response.getBody());
//        String hlValueParam = readHtmlAndFindValueParam(hlNameParam, response.getBody());
        String GALXValueParam = readHtmlAndFindValueParam(GALXNameParam, response.getBody());
//        String timeStmpValueParam = readHtmlAndFindValueParam(timeStmpNameParam, response.getBody());
//        String secTokValueParam = readHtmlAndFindValueParam(secTokNameParam, response.getBody());
        String _utf8ValueParam = readHtmlAndFindValueParam(_utf8NameParam, response.getBody());
        String bgresponseValueParam = readHtmlAndFindValueParam(bgresponseNameParam, response.getBody());
        String rmShownValueParam = readHtmlAndFindValueParam(rmShownNameParam, response.getBody());



        System.out.println(response.getBody());

    }

    private String readHtmlAndFindValueParam(String paramName, String html) {

        int begin = html.indexOf(paramName) + paramName.length();
        int end = html.substring(begin).indexOf("\"");

        return html.substring(begin, begin + end);
    }


}
