package com.pereposter.web.controller

import com.google.common.base.Strings
import grails.plugins.springsecurity.Secured
import grails.validation.ValidationException

class UserController {

    def userService;

    def registration() {}

    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    def save() {

        if (Strings.isNullOrEmpty(params.password)) {
            flash.message = "Passwords is null"
            render(view: 'registration')
            return
        } else if (Strings.isNullOrEmpty(params.passwordRepeat)) {
            flash.message = "Repeat passwords is null"
            render(view: 'registration')
            return
        } else if (params.password != params.passwordRepeat && !Strings.isNullOrEmpty(params.password) && !Strings.isNullOrEmpty(params.passwordRepeat)) {
            flash.message = "Passwords do not match"
            render(view: 'registration')
            return
        }


        try {
            userService.createNewUser(params.login, params.password)
        } catch (ValidationException e) {
            flash.message = e.errors.fieldError.defaultMessage
            render(view: 'registration')
            return
        }

        redirect controller: "login", action: "index"
    }


}
