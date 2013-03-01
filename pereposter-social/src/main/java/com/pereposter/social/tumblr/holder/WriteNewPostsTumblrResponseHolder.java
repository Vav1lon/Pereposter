package com.pereposter.social.tumblr.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("writeNewPostsTumblrResponseHolder")
public class WriteNewPostsTumblrResponseHolder extends ResponseHolder<ResponseObject<String>> {
}
