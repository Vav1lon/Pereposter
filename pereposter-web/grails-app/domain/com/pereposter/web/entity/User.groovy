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

    static hasMany = [accounts: UserSocialAccount]

    static constraints = {
        username blank: false, unique: true, email: true
        password blank: false, minSize: 8
        active blank: false, default: false
    }

    static mapping = {
        table name: 'USER_PEREPOSTER'
        password column: '`password`'
        accounts cascade: 'all-delete-orphan'
        version false
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByPereposterUser(this).collect { it.pereposterRole } as Set
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
