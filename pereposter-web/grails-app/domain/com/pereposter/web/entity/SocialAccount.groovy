package com.pereposter.web.entity

import org.joda.time.DateTime
import org.joda.time.contrib.hibernate.PersistentDateTime

class SocialAccount {

    DateTime createDateLastPost
    boolean enabled
    String lastPostId
    String password
    SocialNetworkEnum socialNetwork
    String socialUserId
    String username
    String name

    static belongsTo = [user: User]

    static mappedBy = []

    static mapping = {
        table name: 'SOCIAL_ACCOUNT'
        socialNetwork enumClass: 'com.pereposter.web.entity.SocialNetworkEnum'
        createDateLastPost type: PersistentDateTime
        user cascade: 'save-update'
        id generator: 'sequence', params: [sequence: 'social_account_seq']
    }

    static constraints = {
        createDateLastPost nullable: true
        lastPostId nullable: true
        socialUserId nullable: true
    }

}
