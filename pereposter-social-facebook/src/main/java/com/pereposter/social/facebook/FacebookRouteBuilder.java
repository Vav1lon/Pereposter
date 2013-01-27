package com.pereposter.social.facebook;

import com.pereposter.social.api.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("facebookRouteBuilder")
public class FacebookRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(Constants.SOCIAL_FACEBOOK_POST_WRITE_REQUEST_QUEUE).beanRef("facebookConnectorWrapper", "writeNewPost").to(Constants.SOCIAL_FACEBOOK_POST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_FACEBOOK_POST_LIST_WRITE_REQUEST_QUEUE).beanRef("facebookConnectorWrapper", "writeNewPosts").to(Constants.SOCIAL_FACEBOOK_POST_LIST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_FACEBOOK_POST_FIND_BY_ID_REQUEST_QUEUE).beanRef("facebookConnectorWrapper", "findPostById").to(Constants.SOCIAL_FACEBOOK_POST_FIND_BY_ID_RESPONSE_QUEUE);
        from(Constants.SOCIAL_FACEBOOK_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE).beanRef("facebookConnectorWrapper", "findPostsByOverCreatedDate").to(Constants.SOCIAL_FACEBOOK_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_FACEBOOK_POST_FIND_LAST_POST_REQUEST_QUEUE).beanRef("facebookConnectorWrapper", "findLastPost").to(Constants.SOCIAL_FACEBOOK_POST_FIND_LAST_POST_RESPONSE_QUEUE);

    }

}