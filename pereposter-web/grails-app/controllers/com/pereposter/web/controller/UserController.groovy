package com.pereposter.web.controller

import com.pereposter.entity.SocialNetworkEnum
import com.pereposter.web.entity.internal.SocialAccount

class UserController {

    def index() {

        List<SocialAccount> accounts = [new SocialAccount(name: 'test', socialNetwork: SocialNetworkEnum.FACEBOOK, enabled: true), new SocialAccount(name: 'test2', socialNetwork: SocialNetworkEnum.TWITTER, enabled: false)]

        render view: 'index', model: [accounts: accounts]
    }

    def registration() {}

    def addSocialNetwork() {}

    def edit() {}

}
