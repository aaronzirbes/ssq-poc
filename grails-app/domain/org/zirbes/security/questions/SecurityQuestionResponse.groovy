package org.zirbes.security.questions

class SecurityQuestionResponse {

    User user
    SecurityQuestion securityQuestion
    String response

    static constraints = {
        user()
        securityQuestion()
        response()
    }
}
