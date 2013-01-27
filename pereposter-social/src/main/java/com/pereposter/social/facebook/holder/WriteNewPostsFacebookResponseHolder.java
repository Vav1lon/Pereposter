package com.pereposter.social.facebook.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("writeNewPostsFacebookResponseHolder")
public class WriteNewPostsFacebookResponseHolder extends ResponseHolder<ResponseObject<String>> {
}
