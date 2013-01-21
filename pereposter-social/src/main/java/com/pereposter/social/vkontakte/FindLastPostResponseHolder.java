package com.pereposter.social.vkontakte;

import com.pereposter.social.ResponseHolder;
import com.pereposter.social.api.entity.PostEntity;
import org.springframework.stereotype.Component;

@Component("findLastPostResponseHolder")
public class FindLastPostResponseHolder extends ResponseHolder<PostEntity> {
}
