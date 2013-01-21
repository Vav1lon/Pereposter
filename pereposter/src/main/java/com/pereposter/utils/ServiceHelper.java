package com.pereposter.utils;

import com.google.common.base.Strings;
import com.pereposter.control.social.SocialNetworkControl;
import com.pereposter.entity.internal.SocialNetworkEnum;
import com.pereposter.entity.internal.UserSocialAccount;
import com.pereposter.entity.SocialAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ServiceHelper {

    @Autowired
    private ApplicationContext applicationContext;

    public SocialNetworkControl getSocialNetworkControl(SocialNetworkEnum socialNetworkEnum) {
        return applicationContext.getBean(socialNetworkEnum.getServiceName(), SocialNetworkControl.class);
    }

    public SocialAuth transformSocialAccount(UserSocialAccount account) {

        SocialAuth result = new SocialAuth();

        result.setLogin(account.getUsername());
        result.setPassword(account.getPassword());

        if (!Strings.isNullOrEmpty(account.getSocialUserId())) {
            result.setUserId(account.getSocialUserId());
        }

        return result;
    }

}
