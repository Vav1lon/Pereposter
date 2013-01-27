package com.pereposter.social.facebook.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findLastPostFacebookResponseHolder")
public class FindLastPostFacebookResponseHolder extends ResponseHolder<ResponseObject<PostEntity>> {
}
