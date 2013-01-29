package com.pereposter.web.controller

import com.google.common.base.Strings
import groovyx.net.http.HTTPBuilder

class SocialBoardController {

    def socialBoardService

    def index() {
        render view: "list", model: [socialNetworks: socialBoardService.list()]
    }

    def addNewScoailNetwork() {

        if (Strings.isNullOrEmpty(params.socialNetworkId) || Strings.isNullOrEmpty(params.login) || Strings.isNullOrEmpty(params.password)) {
            flash.addSocialNetworkError = "Ошибка, не все поля заполнены!"
            redirect(uri: '/')
            return
        }

        String username = params.login as String
        String password = params.password as String
        Integer socialNetworkId = params.socialNetworkId as Integer

        Long id = socialBoardService.addSocialNetwork(socialNetworkId, username, password)

        //TODO: send init request
        def http = new HTTPBuilder('http://localhost:8096')

        def html = http.get(path: "/service/User/" + id.toString())

        redirect(uri: '/')
    }

    def enabledAndDisabledSocialNetwork() {

        long id = params.id as Long


        socialBoardService.enabledAndDisabledSocialNetwork(id)

        redirect(uri: '/')
    }

    def removeSocialNetwork() {

        long id = params.id as Long

        socialBoardService.removedSocialNetwork(id)

        flash.removeSucculMessage = "Соц. сеть успешно удалена"

        redirect(uri: '/')
    }

}
