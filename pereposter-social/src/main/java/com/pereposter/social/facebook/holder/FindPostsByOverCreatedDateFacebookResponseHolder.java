package com.pereposter.social.facebook.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostsResponse;
import org.springframework.stereotype.Component;

@Component("findPostsByOverCreatedDateFacebookResponseHolder")
public class FindPostsByOverCreatedDateFacebookResponseHolder extends ResponseHolder<PostsResponse> {
}
