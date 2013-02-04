package com.pereposter.social.twitter.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findPostByIdTwitterResponseHolder")
public class FindPostByIdTwitterResponseHolder extends ResponseHolder<ResponseObject<PostEntity>> {
}
