class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                Role adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
                Role userRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)

                User ajz = new User(username: 'ajz', enabled: true, password: 'password')
                ajz.save(flush: true)

                UserRole.create ajz, adminRole, true

                assert User.count() == 1
                assert Role.count() == 2
                assert UserRole.count() == 1
            }
        }
    }
    def destroy = {
    }
}
