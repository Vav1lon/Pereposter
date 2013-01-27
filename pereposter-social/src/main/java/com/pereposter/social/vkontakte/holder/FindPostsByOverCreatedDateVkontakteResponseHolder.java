package com.pereposter.social.vkontakte.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostsByOverCreatedDateVkontakteResponseHolder")
public class FindPostsByOverCreatedDateVkontakteResponseHolder extends ResponseHolder<ResponseObject<PostsResponse>> {
}
