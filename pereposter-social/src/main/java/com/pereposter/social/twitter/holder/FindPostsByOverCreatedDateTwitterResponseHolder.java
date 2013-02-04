package com.pereposter.social.twitter.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostsByOverCreatedDateTwitterResponseHolder")
public class FindPostsByOverCreatedDateTwitterResponseHolder extends ResponseHolder<ResponseObject<PostsResponse>> {
}
