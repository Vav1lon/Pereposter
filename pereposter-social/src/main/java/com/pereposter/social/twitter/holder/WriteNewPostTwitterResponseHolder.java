package com.pereposter.social.twitter.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("writeNewPostTwitterResponseHolder")
public class WriteNewPostTwitterResponseHolder extends ResponseHolder<ResponseObject<String>> {
}
