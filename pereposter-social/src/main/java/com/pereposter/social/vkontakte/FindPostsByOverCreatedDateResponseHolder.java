package com.pereposter.social.vkontakte;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostsResponse;
import org.springframework.stereotype.Component;

@Component("findPostsByOverCreatedDateResponseHolder")
public class FindPostsByOverCreatedDateResponseHolder extends ResponseHolder<PostsResponse> {
}
