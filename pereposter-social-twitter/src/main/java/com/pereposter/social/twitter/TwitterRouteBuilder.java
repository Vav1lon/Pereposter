package com.pereposter.social.twitter;

import com.pereposter.social.api.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("twitterRouteBuilder")
public class TwitterRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(Constants.SOCIAL_TWITTER_POST_WRITE_REQUEST_QUEUE).beanRef("twitterConnectorWrapper", "writeNewPost").to(Constants.SOCIAL_TWITTER_POST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TWITTER_POST_LIST_WRITE_REQUEST_QUEUE).beanRef("twitterConnectorWrapper", "writeNewPosts").to(Constants.SOCIAL_TWITTER_POST_LIST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TWITTER_POST_FIND_BY_ID_REQUEST_QUEUE).beanRef("twitterConnectorWrapper", "findPostById").to(Constants.SOCIAL_TWITTER_POST_FIND_BY_ID_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TWITTER_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE).beanRef("twitterConnectorWrapper", "findPostsByOverCreatedDate").to(Constants.SOCIAL_TWITTER_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_TWITTER_POST_FIND_LAST_POST_REQUEST_QUEUE).beanRef("twitterConnectorWrapper", "findLastPost").to(Constants.SOCIAL_TWITTER_POST_FIND_LAST_POST_RESPONSE_QUEUE);

    }

}