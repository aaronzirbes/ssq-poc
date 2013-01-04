package src.groovy.org.zirbes.security.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.SpringSecurityCoreVersion

/**
 * An {@link org.springframework.security.core.Authentication} implementation that is designed
 * to allow a remember aspect tied to a username password authentication.  This is used by the
 * security question implementaiton to allow the user to not have to answer security questions
 * more than once from a trusted client.
 */
class SecurityQuestionAuthenticationToken extends AbstractAuthenticationToken {
    
    private static final long serialVersionUID = 1

    //~ Instance Fields ================================================================================================

    final Object principal
    final Map<String, String> securityQuestionResponses = [:]

    //~ Constructors ===================================================================================================

   /**      
     * This constructor can be safely used by any code that wishes to create a
     * <code>SecurityQuestionAuthenticationToken</code>, as the 
     * {@link#isAuthenticated()} will return <code>false</code>.
     *
     * @param principal
     * @param securityQuestionResponses
    */
    public SecurityQuestionAuthenticationToken(Object principal, 
                                               Map<String, String> securityQuestionResponses) {
        super(null)
        if (!principal || !securityQuestionResponses) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor")
        }
        this.principal = principal
        this.securityQuestionResponses = securityQuestionResponses
        setAuthenticated(false)
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or <code>AuthenticationProvider</code>
     * implementations that are satisfied with producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal
     * @param credentials
     * @param key
     * @param authorities
     */
    public SecurityQuestionAuthenticationToken(Object principal, 
                                               Map<String, String> securityQuestionResponses,
                                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities)
        this.principal = principal
        this.securityQuestionResponses = securityQuestionResponses
        super.setAuthenticated(true)
    }

    //~ Methods ========================================================================================================

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead")
        }
        super.setAuthenticated(false)
    }
}
