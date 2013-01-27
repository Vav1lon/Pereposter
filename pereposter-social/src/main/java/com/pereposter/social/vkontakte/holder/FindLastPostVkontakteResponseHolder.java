package com.pereposter.social.vkontakte.holder;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import com.pereposter.social.api.entity.ResponseObject;
import org.springframework.stereotype.Component;

@Component("findLastPostVkontakteResponseHolder")
public class FindLastPostVkontakteResponseHolder extends ResponseHolder<ResponseObject<PostEntity>> {
}
