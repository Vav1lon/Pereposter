package com.pereposter.utils;

import com.google.common.base.Strings;
import com.pereposter.control.social.SocialControl;
import com.pereposter.entity.internal.SocialNetworkEnum;
import com.pereposter.entity.internal.SocialUserAccount;
import com.pereposter.social.api.entity.SocialAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ServiceHelper {

    @Autowired
    private ApplicationContext applicationContext;

    public SocialControl getSocialNetworkControl(SocialNetworkEnum socialNetworkEnum) {
        return applicationContext.getBean(socialNetworkEnum.getServiceName(), SocialControl.class);
    }

    public SocialAuthEntity transformSocialAuthService(SocialUserAccount account) {

        SocialAuthEntity result = new SocialAuthEntity();

        result.setLogin(account.getUsername());
        result.setPassword(account.getPassword());

        if (!Strings.isNullOrEmpty(account.getSocialUserId())) {
            result.setUserId(account.getSocialUserId());
        }

        return result;
    }

}
