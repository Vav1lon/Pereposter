package com.pereposter.web.entity

class Role {

    String authority

    static mapping = {
        table name: 'ROLE'
        cache true
        version false
    }

    static constraints = {
        authority blank: false, unique: true
    }
}
