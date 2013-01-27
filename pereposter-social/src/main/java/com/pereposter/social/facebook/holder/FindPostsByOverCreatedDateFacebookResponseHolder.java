package com.pereposter.social.facebook.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostsByOverCreatedDateFacebookResponseHolder")
public class FindPostsByOverCreatedDateFacebookResponseHolder extends ResponseHolder<ResponseObject<PostsResponse>> {
}
