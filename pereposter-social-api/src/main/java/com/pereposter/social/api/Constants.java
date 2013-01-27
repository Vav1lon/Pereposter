package com.pereposter.social.api;

public interface Constants {
    String REQUEST_ID = "REQUEST_ID";
    String OPERATION_NAME = "OPERATION_NAME";

    // Vkontakte

    String SOCIAL_VKONTAKTE_POST_WRITE_REQUEST_QUEUE = "jms://queue:social.vkontakte.post.write.request.queue";
    String SOCIAL_VKONTAKTE_POST_WRITE_RESPONSE_QUEUE = "jms://queue:social.vkontakte.post.write.response.queue";

    String SOCIAL_VKONTAKTE_POST_LIST_WRITE_REQUEST_QUEUE = "jms://queue:social.vkontakte.post.list.write.request.queue";
    String SOCIAL_VKONTAKTE_POST_LIST_WRITE_RESPONSE_QUEUE = "jms://queue:social.vkontakte.post.list.write.response.queue";

    String SOCIAL_VKONTAKTE_POST_FIND_BY_ID_REQUEST_QUEUE = "jms://queue:social.vkontakte.post.findById.request.queue";
    String SOCIAL_VKONTAKTE_POST_FIND_BY_ID_RESPONSE_QUEUE = "jms://queue:social.vkontakte.post.findById.response.queue";

    String SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE = "jms://queue:social.vkontakte.post.findByOverCreateDate.request.queue";
    String SOCIAL_VKONTAKTE_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE = "jms://queue:social.vkontakte.post.findByOverCreateDate.response.queue";

    String SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_REQUEST_QUEUE = "jms://queue:social.vkontakte.post.findLastPost.request.queue";
    String SOCIAL_VKONTAKTE_POST_FIND_LAST_POST_RESPONSE_QUEUE = "jms://queue:social.vkontakte.post.findLastPost.response.queue";

    // Facebook

    String SOCIAL_FACEBOOK_POST_WRITE_REQUEST_QUEUE = "jms://queue:social.facebook.post.write.request.queue";
    String SOCIAL_FACEBOOK_POST_WRITE_RESPONSE_QUEUE = "jms://queue:social.facebook.post.write.response.queue";

    String SOCIAL_FACEBOOK_POST_LIST_WRITE_REQUEST_QUEUE = "jms://queue:social.facebook.post.list.write.request.queue";
    String SOCIAL_FACEBOOK_POST_LIST_WRITE_RESPONSE_QUEUE = "jms://queue:social.facebook.post.list.write.response.queue";

    String SOCIAL_FACEBOOK_POST_FIND_BY_ID_REQUEST_QUEUE = "jms://queue:social.facebook.post.findById.request.queue";
    String SOCIAL_FACEBOOK_POST_FIND_BY_ID_RESPONSE_QUEUE = "jms://queue:social.facebook.post.findById.response.queue";

    String SOCIAL_FACEBOOK_POST_FIND_BY_OVER_CREATE_DATE_REQUEST_QUEUE = "jms://queue:social.facebook.post.findByOverCreateDate.request.queue";
    String SOCIAL_FACEBOOK_POST_FIND_BY_OVER_CREATE_DATE_RESPONSE_QUEUE = "jms://queue:social.facebook.post.findByOverCreateDate.response.queue";

    String SOCIAL_FACEBOOK_POST_FIND_LAST_POST_REQUEST_QUEUE = "jms://queue:social.facebook.post.findLastPost.request.queue";
    String SOCIAL_FACEBOOK_POST_FIND_LAST_POST_RESPONSE_QUEUE = "jms://queue:social.facebook.post.findLastPost.response.queue";


}
