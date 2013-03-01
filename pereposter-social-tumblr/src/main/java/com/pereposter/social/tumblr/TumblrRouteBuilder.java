package com.pereposter.social.tumblr;

import com.pereposter.social.api.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("tumblrRouteBuilder")
public class TumblrRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(Constants.SOCIAL_TUMBLR_POST_WRITE_REQUEST_QUEUE).beanRef("tumblrConnectorWrapper", "writeNewPost").to(Constants.SOCIAL_TUMBLR_POST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TUMBLR_POST_LIST_WRITE_REQUEST_QUEUE).beanRef("tumblrConnectorWrapper", "writeNewPosts").to(Constants.SOCIAL_TUMBLR_POST_LIST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TUMBLR_POST_FIND_BY_ID_REQUEST_QUEUE).beanRef("tumblrConnectorWrapper", "findPostById").to(Constants.SOCIAL_TUMBLR_POST_FIND_BY_ID_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TUMBLR_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE).beanRef("tumblrConnectorWrapper", "findPostsByOverCreatedDate").to(Constants.SOCIAL_TUMBLR_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TUMBLR_POST_FIND_LAST_POST_REQUEST_QUEUE).beanRef("tumblrConnectorWrapper", "findLastPost").to(Constants.SOCIAL_TUMBLR_POST_FIND_LAST_POST_RESPONSE_QUEUE);

    }

}