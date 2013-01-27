package com.pereposter.social.facebook.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("writeNewPostFacebookResponseHolder")
public class WriteNewPostFacebookResponseHolder extends ResponseHolder<ResponseObject<String>> {
}
