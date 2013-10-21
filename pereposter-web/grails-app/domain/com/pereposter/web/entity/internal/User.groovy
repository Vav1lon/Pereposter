package com.pereposter.web.entity.internal

class User {

    transient springSecurityService

    String username
    String password
    String email
    boolean enabled = true
    boolean accountExpired = false
    boolean accountLocked = false
    boolean passwordExpired = false
    Date dateCreated
    Date lastUpdated
    List accounts

    static transients = ['springSecurityService']

    static hasMany = [accounts: SocialAccount]

    static constraints = {
        username blank: false
        password blank: false, size: 8..512
        email email: true, unique: true
    }

    static mapping = {
        table name: 'SITE_USER'
        password column: '`password`'
        accounts lazy: true
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
