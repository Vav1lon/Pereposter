package com.pereposter.social.googleplus.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostsResponse;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostsByOverCreatedDateGooglePlusResponseHolder")
public class FindPostsByOverCreatedDateGooglePlusResponseHolder extends ResponseHolder<ResponseObject<PostsResponse>> {
}
