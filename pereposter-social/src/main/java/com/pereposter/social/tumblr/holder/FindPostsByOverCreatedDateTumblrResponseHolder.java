package com.pereposter.social.tumblr.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostsByOverCreatedDateTumblrResponseHolder")
public class FindPostsByOverCreatedDateTumblrResponseHolder extends ResponseHolder<ResponseObject<PostsResponse>> {
}
