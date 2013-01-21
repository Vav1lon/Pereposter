package com.pereposter.social;

import com.pereposter.social.api.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("socialRouteBuilder")
public class SocialRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(Constants.SOCIAL_VKONTAKTE_POST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_LIST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostsResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_ID_RESPONSE_QUEUE).beanRef("findPostByIdResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE).beanRef("findPostsByOverCreatedDateResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_RESPONSE_QUEUE).beanRef("findLastPostResponseHolder", "addResponse");

    }
}
