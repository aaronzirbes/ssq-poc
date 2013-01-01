package org.zirbes.security.questions

import grails.plugins.springsecurity.Secured

class TopSecretController {

    def index() { }

    @Secured(['ROLE_ADMIN'])
    def stuff() { }
}
