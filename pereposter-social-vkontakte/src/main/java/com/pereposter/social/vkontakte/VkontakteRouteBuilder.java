package com.pereposter.social.vkontakte;

import com.pereposter.social.api.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("vkontakteRouteBuilder")
public class VkontakteRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(Constants.SOCIAL_VKONTAKTE_POST_WRITE_REQUEST_QUEUE).beanRef("vkontakteConnectorWrapper", "writeNewPost").to(Constants.SOCIAL_VKONTAKTE_POST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_VKONTAKTE_POST_LIST_WRITE_REQUEST_QUEUE).beanRef("vkontakteConnectorWrapper", "writeNewPosts").to(Constants.SOCIAL_VKONTAKTE_POST_LIST_WRITE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_ID_REQUEST_QUEUE).beanRef("vkontakteConnectorWrapper", "findPostById").to(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_ID_RESPONSE_QUEUE);
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE).beanRef("vkontakteConnectorWrapper", "findPostsByOverCreatedDate").to(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE);
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_REQUEST_QUEUE).beanRef("vkontakteConnectorWrapper", "findLastPost").to(Constants.SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_RESPONSE_QUEUE);

    }

}