package com.pereposter.social.googleplus;

import com.pereposter.social.api.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("googlePlusRouteBuilder")
public class GooglePlusRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from(Constants.SOCIAL_GOOGLEPLUS_POST_WRITE_REQUEST_QUEUE).beanRef("googlePlusConnectorWrapper", "writeNewPost").to(Constants.SOCIAL_GOOGLEPLUS_POST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_GOOGLEPLUS_POST_LIST_WRITE_REQUEST_QUEUE).beanRef("googlePlusConnectorWrapper", "writeNewPosts").to(Constants.SOCIAL_GOOGLEPLUS_POST_LIST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_BY_ID_REQUEST_QUEUE).beanRef("googlePlusConnectorWrapper", "findPostById").to(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_BY_ID_RESPONSE_QUEUE);
        from(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE).beanRef("googlePlusConnectorWrapper", "findPostsByOverCreatedDate").to(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_LAST_POST_REQUEST_QUEUE).beanRef("googlePlusConnectorWrapper", "findLastPost").to(Constants.SOCIAL_GOOGLEPLUS_POST_FIND_LAST_POST_RESPONSE_QUEUE);
    }

}