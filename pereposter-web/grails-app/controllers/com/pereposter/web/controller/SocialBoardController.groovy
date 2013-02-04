package com.pereposter.web.controller

import com.google.common.base.Charsets
import com.google.common.base.Strings
import com.google.common.io.CharStreams
import groovy.json.JsonSlurper
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient

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

        sendRequestToCore(id)

        redirect(uri: '/')
    }

    def validateSocialNetwokAccount() {
        Long idInternalSocialNetwork = params.id as long

        sendRequestToCore(idInternalSocialNetwork)

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

    //TODO: need refactoring
    private void sendRequestToCore(long idInternalSocialNetwork) {

        try {
            HttpClient client = new DefaultHttpClient()
            HttpGet get = new HttpGet(grailsApplication.config.pereposter.core.initSocialAccount.url + idInternalSocialNetwork)

            String body;
            HttpResponse httpResponse = client.execute(get)
            if (httpResponse.getEntity() != null) {
                body = CharStreams.toString(new InputStreamReader(httpResponse.getEntity().getContent(), Charsets.UTF_8));
                Map response = new JsonSlurper().parseText(body)
            }
            get.abort()

            if (body != null) {

                log.error('====== Begin ======')
                log.error(grailsApplication.config.pereposter.core.initSocialAccount.url + idInternalSocialNetwork)
                log.error(body)
                log.error('====== End ======')

                flash.errorMessage = response['error']

                //flash.errorMessage = "Не удалось получить данные учетной записи, проверти логин/пароль"
            }
        } catch (Exception e) {
            log.error(e.getMessage())
            flash.errorMessage = "Сервер валидации пользователя недоступен"
        }

    }

}
