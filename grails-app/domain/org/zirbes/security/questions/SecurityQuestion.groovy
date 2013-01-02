package org.zirbes.security.questions

import static java.util.UUID.randomUUID

class SecurityQuestion {

    String uuid = randomUUID() as String
    String question

    String toString() { question }

    boolean answer(User user, String response) {
        SecurityQuestionResponse sqr = SecurityQuestionResponse.findByUserAndSecurityQuestion(user, this)
        if (sqr) {
            (response.toLowerCase().trim() == sqr.response.toLowerCase().trim())
        } else { false }
    }

    static constraints = {
        uuid(maxSize:36)
        question(maxSize:250)
    }
}
