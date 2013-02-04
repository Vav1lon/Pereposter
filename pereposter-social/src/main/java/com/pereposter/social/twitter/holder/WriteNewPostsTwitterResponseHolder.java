package com.pereposter.social.twitter.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("writeNewPostsTwitterResponseHolder")
public class WriteNewPostsTwitterResponseHolder extends ResponseHolder<ResponseObject<String>> {
}
