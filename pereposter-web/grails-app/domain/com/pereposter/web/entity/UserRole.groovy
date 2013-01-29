package com.pereposter.web.entity

import org.apache.commons.lang.builder.HashCodeBuilder

class UserRole implements Serializable {

    User pereposterUser
    Role pereposterRole

    boolean equals(other) {
        if (!(other instanceof UserRole)) {
            return false
        }

        other.pereposterUser?.id == pereposterUser?.id &&
                other.pereposterRole?.id == pereposterRole?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (pereposterUser) builder.append(pereposterUser.id)
        if (pereposterRole) builder.append(pereposterRole.id)
        builder.toHashCode()
    }

    static UserRole get(long pereposterUserId, long pereposterRoleId) {
        find 'from UserRole where pereposterUser.id=:pereposterUserId and pereposterRole.id=:pereposterRoleId',
                [pereposterUserId: pereposterUserId, pereposterRoleId: pereposterRoleId]
    }

    static UserRole create(User pereposterUser, Role pereposterRole, boolean flush = false) {
        new UserRole(pereposterUser: pereposterUser, pereposterRole: pereposterRole).save(flush: flush, insert: true)
    }

    static boolean remove(User pereposterUser, Role pereposterRole, boolean flush = false) {
        UserRole instance = UserRole.findByPereposterUserAndPereposterRole(pereposterUser, pereposterRole)
        if (!instance) {
            return false
        }

        instance.delete(flush: flush)
        true
    }

    static void removeAll(User pereposterUser) {
        executeUpdate 'DELETE FROM UserRole WHERE pereposterUser=:pereposterUser', [pereposterUser: pereposterUser]
    }

    static void removeAll(Role pereposterRole) {
        executeUpdate 'DELETE FROM UserRole WHERE pereposterRole=:pereposterRole', [pereposterRole: pereposterRole]
    }

    static mapping = {
        table name:  'USER_ROLE'
        id composite: ['pereposterRole', 'pereposterUser']
        version false
    }
}
