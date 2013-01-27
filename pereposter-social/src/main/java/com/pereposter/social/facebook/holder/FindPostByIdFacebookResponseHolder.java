package com.pereposter.social.facebook.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostByIdFacebookResponseHolder")
public class FindPostByIdFacebookResponseHolder extends ResponseHolder<ResponseObject<PostEntity>> {
}
