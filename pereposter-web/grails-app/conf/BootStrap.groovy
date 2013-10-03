import com.pereposter.web.entity.Role
import com.pereposter.web.entity.User
import com.pereposter.web.entity.UserRole

class BootStrap {

    def init = { servletContext ->

        Role role1 = Role.findByAuthority('ROLE_USER');

        if (!role1) {
            role1 = new Role(authority: 'ROLE_USER')
            role1.save()
        }

        Role role2 = Role.findByAuthority('ROLE_ADMIN');

        if (!role2) {
            role2 = new Role(authority: 'ROLE_ADMIN')
            role2.save()
        }

        Role role3 = Role.findByAuthority('ROLE_INVITE');

        if (!role3) {
            role3 = new Role(authority: 'ROLE_INVITE')
            role3.save()
        }

        User user = User.findByUsername('dev@vav1lon.ru')
        if (!user) {
            user = new User(username: 'dev@vav1lon.ru', password: '19216811', active: true, accountExpired: false, accountLocked: false, enabled: true)
            user.save(flush: true)

            UserRole.create(user, role1, true)
            UserRole.create(user, role2, true)
            UserRole.create(user, role3, true)
        }


    }
    def destroy = {
    }
}
