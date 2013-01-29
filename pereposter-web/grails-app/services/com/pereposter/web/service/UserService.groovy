package com.pereposter.web.service

import com.pereposter.web.entity.Role
import com.pereposter.web.entity.User
import com.pereposter.web.entity.UserRole

class UserService {

    void createNewUser(String login, String password) {

        User newUser = new User(username: login
                , password: password
                , enabled: true
                , active: false
                , accountExpired: false
                , passwordExpired: false
                , accountLocked: false)
        newUser.save();

        UserRole.create(newUser, Role.findByAuthority("ROLE_USER"), true)
    }
}

