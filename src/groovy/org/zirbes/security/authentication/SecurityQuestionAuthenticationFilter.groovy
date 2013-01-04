package src.groovy.org.zirbes.security.authentication

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.SpringSecurityCoreVersion
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY

class SecurityQuestionAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    //~ Static fields/initializers =====================================================================================

    public static final String SPRING_SECURITY_FORM_QUESTION_KEY = 'j_security_question'
    public static final String SPRING_SECURITY_FORM_ANSWER_KEY = 'j_security_answer'

    String questionParameter = SPRING_SECURITY_FORM_QUESTION_KEY
    String answerParameter = SPRING_SECURITY_FORM_ANSWER_KEY
    boolean postOnly = true

    //~ Constructors ===================================================================================================

    public SecurityQuestionAuthenticationFilter() {
        super('/j_spring_security_question_check')
    }

    //~ Methods ========================================================================================================

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.method == 'POST') {
            throw new AuthenticationServiceException('Authentication method not supported: ' + request.method)
        }

        // Security Question Authentication requires an existing authenticated token
        Authentication currentAuthenticationToken = SecurityContextHolder.getContext().getAuthentication()

        // Get the questions/answers from the request object
        Map<String, String> securityQuestions = obtainSecurityQuestions(request) ?: [:]

        SecurityQuestionAuthenticationToken authenticationToken = 
            new SecurityQuestionAuthenticationToken(currentAuthenticationToken.principal, securityQuestions)


        // Allow subclasses to set the 'details' property
        setDetails(request, authenticationToken)

        if (!authenticationToken.details && currentAuthenticationToken.details) {
            authenticationToken.details = currentAuthenticationToken.details
        }

        return this.getAuthenticationManager().authenticate(authenticationToken)
    }

    /**
     * Enables subclasses to override the composition of the password, such as by including additional values
     * and a separator.<p>This might be used for example if a postcode/zipcode was required in addition to the
     * password. A delimiter such as a pipe (|) should be used to separate the password and extended value(s). The
     * <code>AuthenticationDao</code> will need to generate the expected password in a corresponding manner.</p>
     *
     * @param request so that request attributes can be retrieved
     *
     * @return the password that will be presented in the <code>Authentication</code> request token to the
     *         <code>AuthenticationManager</code>
     */
    protected Map<String, String> obtainSecurityQuestions(HttpServletRequest request) {
        Map<String, String> securityQuestionResponses = [:]
        List<String> questions = request.getParameters(questionParameter)
        List<String> answers = request.getParameters(answerParameter)
        questions.eachWithIndex{ q, i ->
            securityQuestionResponses[q] = answers[i]
        }
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication request's details
     * property.
     *
     * @param request that an authentication request is being created for
     * @param authenticationToken the authentication request object that should have its details set
     */
    protected void setDetails(HttpServletRequest request, 
                              UsernamePasswordRememberMeAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request))
    }
}
