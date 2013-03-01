package com.pereposter.social.tumblr.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostByIdTumblrResponseHolder")
public class FindPostByIdTumblrResponseHolder extends ResponseHolder<ResponseObject<PostEntity>> {
}
