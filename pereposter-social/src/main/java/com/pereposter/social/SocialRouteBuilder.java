package com.pereposter.social;

import com.pereposter.social.api.Constants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component("socialRouteBuilder")
public class SocialRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from(Constants.SOCIAL_VKONTAKTE_POST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostVkontakteResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_LIST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostsVkontakteResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_ID_RESPONSE_QUEUE).beanRef("findPostByIdVkontakteResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE).beanRef("findPostsByOverCreatedDateVkontakteResponseHolder", "addResponse");
        from(Constants.SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_RESPONSE_QUEUE).beanRef("findLastPostVkontakteResponseHolder", "addResponse");

        from(Constants.SOCIAL_FACEBOOK_POST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostFacebookResponseHolder", "addResponse");
        from(Constants.SOCIAL_FACEBOOK_POST_LIST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostsFacebookResponseHolder", "addResponse");
        from(Constants.SOCIAL_FACEBOOK_POST_FIND_BY_ID_RESPONSE_QUEUE).beanRef("findPostByIdFacebookResponseHolder", "addResponse");
        from(Constants.SOCIAL_FACEBOOK_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE).beanRef("findPostsByOverCreatedDateFacebookResponseHolder", "addResponse");
        from(Constants.SOCIAL_FACEBOOK_POST_FIND_LAST_POST_RESPONSE_QUEUE).beanRef("findLastPostFacebookResponseHolder", "addResponse");

        from(Constants.SOCIAL_TUMBLR_POST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostTumblrResponseHolder", "addResponse");
        from(Constants.SOCIAL_TUMBLR_POST_LIST_WRITE_RESPONSE_QUEUE).beanRef("writeNewPostsTumblrResponseHolder", "addResponse");
        from(Constants.SOCIAL_TUMBLR_POST_FIND_BY_ID_RESPONSE_QUEUE).beanRef("findPostByIdTumblrResponseHolder", "addResponse");
        from(Constants.SOCIAL_TUMBLR_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE).beanRef("findPostsByOverCreatedDateTumblrResponseHolder", "addResponse");
        from(Constants.SOCIAL_TUMBLR_POST_FIND_LAST_POST_RESPONSE_QUEUE).beanRef("findLastPostTumblrResponseHolder", "addResponse");

    }
}
