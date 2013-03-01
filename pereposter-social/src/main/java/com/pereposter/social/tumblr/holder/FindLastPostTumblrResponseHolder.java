package com.pereposter.social.tumblr.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findLastPostTumblrResponseHolder")
public class FindLastPostTumblrResponseHolder extends ResponseHolder<ResponseObject<PostEntity>> {
}
