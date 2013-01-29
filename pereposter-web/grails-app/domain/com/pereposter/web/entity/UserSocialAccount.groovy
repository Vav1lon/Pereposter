package com.pereposter.web.entity

import org.joda.time.DateTime
import org.joda.time.contrib.hibernate.PersistentDateTime

class UserSocialAccount {

    DateTime createDateLastPost
    boolean enabled
    String lastPostId
    String password
    SocialNetworkEnum socialNetwork
    String socialUserId
    String username

    static hasOne = [user: User]

    static mapping = {
        table name: 'USER_SOCIAL_ACCOUNT'
        socialNetwork enumClass: 'com.pereposter.web.entity.SocialNetworkEnum'
        createDateLastPost type: PersistentDateTime
        user cascade: 'save-update'

        version false
    }

    static constraints = {
        createDateLastPost nullable: true
        lastPostId nullable: true
        socialUserId nullable: true
    }

}
