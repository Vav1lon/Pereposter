package com.pereposter.web.entity

class User {

    transient springSecurityService

    String username
    String password
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    boolean active


    static hasMany = [accounts: SocialAccount]

    static constraints = {
        username blank: false, unique: true, email: true
        password blank: false, minSize: 8
        active blank: false, default: false
        accounts lazy: true;
    }

    static mapping = {
        table name: 'USERS'
        password column: '`password`'
        accounts cascade: 'all-delete-orphan'
        id generator: 'sequence', params: [sequence: 'user_seq']
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }
}
