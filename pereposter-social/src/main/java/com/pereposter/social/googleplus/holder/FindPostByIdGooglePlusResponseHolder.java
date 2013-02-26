package com.pereposter.social.googleplus.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostByIdGooglePlusResponseHolder")
public class FindPostByIdGooglePlusResponseHolder extends ResponseHolder<ResponseObject<PostEntity>> {
}
