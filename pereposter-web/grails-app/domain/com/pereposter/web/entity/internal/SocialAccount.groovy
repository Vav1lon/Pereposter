package com.pereposter.web.entity.internal

import com.pereposter.entity.SocialNetworkEnum
import org.jadira.usertype.dateandtime.joda.PersistentDateTime
import org.joda.time.DateTime

class SocialAccount {

    String name
    String username
    String password
    SocialNetworkEnum socialNetwork
    Boolean enabled = false
    String lastPostId = null
    DateTime createDateLastPost
    String socialUserId = null

    Date dateCreated
    Date lastUpdated

    static hasOne = [user: User]

    static constraints = {

    }

    static mapping = {
        table name: 'SOCIAL_ACCOUNT'
        password column: '`password`'
        id generator: 'sequence', params: [sequence: 'social_account_seq']
        socialNetwork enumClass: 'com.pereposter.entity.SocialNetworkEnum'
        createDateLastPost type: PersistentDateTime
    }
}
