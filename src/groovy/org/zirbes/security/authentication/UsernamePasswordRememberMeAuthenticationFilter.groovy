package src.groovy.org.zirbes.security.authentication

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.codehaus.groovy.grails.plugins.springsecurity.RequestHolderAuthenticationFilter

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY

/**
 * This filter needs to be put before the RequestHolderAuthenticationFilter in the filter chain
 * So it can get process a request with a remember me cookie before a standard login request
 * without the remembered cookie.
 */
class UsernamePasswordRememberMeAuthenticationFilter extends RequestHolderAuthenticationFilter {

    //~ Static fields/initializers =====================================================================================

    public static final String SPRING_SECURITY_QUESTIONS_REMEMBER_ME_COOKIE_KEY = 'SPRING_SECURITY_QUESTIONS_REMEMBER_ME_COOKIE'

    String cookieName = SPRING_SECURITY_QUESTIONS_REMEMBER_ME_COOKIE_KEY

    //~ Methods ========================================================================================================

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, 
                                                HttpServletResponse response) 
            throws AuthenticationException {

        if (postOnly && !request.method == 'POST') {
            throw new AuthenticationServiceException('Authentication method not supported: ' + request.method)
        }

        String username = ( obtainUsername(request) ?: '' ).trim()
        String password =   obtainPassword(request) ?: ''
        String key =        obtainRememberMeKey(request)

        UsernamePasswordRememberMeAuthenticationToken authenticationToken = 
            new UsernamePasswordRememberMeAuthenticationToken(username, password, key)

        // Allow subclasses to set the 'details' property
        setDetails(request, authenticationToken)

        return this.getAuthenticationManager().authenticate(authenticationToken)
    }

    /**
     * Retrieves the remember me cookie from the request object.  This
     * enables subclasses to override this method.
     */
    protected String obtainRememberMeKey(HttpServletRequest request) {
        request.cookies?.find{ it.name == cookieName }?.value
    }

    /**
     * Sets the parameter name which will be used to obtain the username from the login request.
     *
     * @param usernameParameter the parameter name. Defaults to 'j_username'.
     */
    public void setCookieName(String cookieName) {
        Assert.hasText(cookieName, 'cookieName parameter must not be empty or null')
        this.cookieName = cookieName
    }
}

