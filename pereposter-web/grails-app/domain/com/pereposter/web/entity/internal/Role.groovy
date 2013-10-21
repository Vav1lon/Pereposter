package com.pereposter.web.entity.internal

class Role {

    String authority

    static mapping = {
        table name: 'SITE_ROLE'
        cache true
        id generator: 'sequence', params: [sequence: 'role_seq']
    }

    static constraints = {
        authority blank: false, unique: true
    }
}
