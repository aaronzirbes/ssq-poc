package src.groovy.org.zirbes.security.authentication

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.SpringSecurityCoreVersion

/**
 * An {@link org.springframework.security.core.Authentication} implementation that is designed
 * to allow a remember aspect tied to a username password authentication.  This is used by the
 * security question implementaiton to allow the user to not have to answer security questions
 * more than once from a trusted client.
 */
class UsernamePasswordRememberMeAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private static final long serialVersionUID = 1

    //~ Instance Fields ================================================================================================

    private String key

    //~ Constructors ===================================================================================================

    /**      
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordRememberMeAuthenticationToken</code>, as the 
     * {@link#isAuthenticated()} will return <code>false</code>.
     *
     * @param principal
     * @param credentials
     * @param key
    */
    public UsernamePasswordRememberMeAuthenticationToken(Object principal, 
                                                         Object credentials, 
                                                         String key) {
        super(principal, credentials)
        if (!key) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor")
        }

        this.key = key
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
    public UsernamePasswordRememberMeAuthenticationToken(Object principal, 
                                                         Object credentials, 
                                                         String key, 
                                                         Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities)
        this.key = key
    }
}
