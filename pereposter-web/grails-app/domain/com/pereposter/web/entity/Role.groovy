package com.pereposter.web.entity

class Role {

    String authority

    static mapping = {
        table name: 'ROLES'
        cache true
        id generator: 'sequence', params: [sequence: 'role_seq']
    }

    static constraints = {
        authority blank: false, unique: true
    }
}
