import com.pereposter.web.entity.internal.Role
import com.pereposter.web.entity.internal.User
import com.pereposter.web.entity.internal.UserRole

class BootStrap {

    def init = { servletContext ->

        // TEMP DATA

        Role roleUser = Role.findByAuthority('ROLE_USER')
        if (roleUser == null) {
            roleUser = new Role(authority: 'ROLE_USER')
            roleUser.save(flush: true)
        }

        Role roleAdmin = Role.findByAuthority('ROLE_ADMIN')
        if (roleAdmin == null) {
            roleAdmin = new Role(authority: 'ROLE_ADMIN')
            roleAdmin.save(flush: true)
        }

        User userUser = User.findByUsername('user')
        if (userUser == null) {
            userUser = new User(username: 'user', email: 'aaa@aaa.ru', accountExpired: false, accountLocked: false, enabled: true, password: '123456789', passwordExpired: false)
            userUser.save(flush: true)
        }

        User userAdmin = User.findByUsername('admin')
        if (userAdmin == null) {
            userAdmin = new User(username: 'admin', email: 'aaa@1aaa.ru', accountExpired: false, accountLocked: false, enabled: true, password: '123456789', passwordExpired: false)
            userAdmin.save(flush: true)
        }


        if (UserRole.get(userUser.id, roleUser.id) == null) {
            UserRole.create(userUser, roleUser, true)
        }

        if (UserRole.get(userAdmin.id, roleAdmin.id) == null) {
            UserRole.create(userAdmin, roleAdmin, true)
        }


    }
    def destroy = {
    }
}
