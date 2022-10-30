package pwr.smart.home.common.configuration.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Example jwt:
 * {
 *   "exp": 1665434705,
 *   "iat": 1665434405,
 *   "auth_time": 1665434299,
 *   "jti": "86826cfd-d091-4f46-9c3a-17bc4ac91aea",
 *   "iss": "http://localhost:8180/auth/realms/Test-App",
 *   "aud": "account",
 *   "sub": "13262b13-a75a-4f05-86ba-0d28e2a11069",
 *   "typ": "Bearer",
 *   "azp": "test-app",
 *   "nonce": "ee69408c-bbd6-4427-8037-560a666597b0",
 *   "session_state": "d34c03be-0934-4611-a24d-47b2be38d434",
 *   "acr": "0",
 *   "allowed-origins": [
 *     "https://localhost:3000",
 *     "http://localhost:3000"
 *   ],
 *   "realm_access": {
 *     "roles": [
 *       "ROLE_User",
 *       "offline_access",
 *       "default-roles-test-app",
 *       "uma_authorization"
 *     ]
 *   },
 *   "resource_access": {
 *     "test-app": {
 *       "roles": [
 *         "Endpoint_Access"
 *       ]
 *     },
 *     "account": {
 *       "roles": [
 *         "manage-account",
 *         "manage-account-links",
 *         "view-profile"
 *       ]
 *     }
 *   },
 *   "scope": "openid email profile GetEndpoint",
 *   "sid": "d34c03be-0934-4611-a24d-47b2be38d434",
 *   "email_verified": true,
 *   "name": "Kacper Ziejło",
 *   "preferred_username": "test",
 *   "given_name": "Kacper",
 *   "family_name": "Ziejło",
 *   "email": "test@gmail.com"
 * }
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public KeycloakJwtAuthenticationConverter() { }

    @Override
    public AbstractAuthenticationToken convert(final Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(defaultGrantedAuthoritiesConverter.convert(jwt).stream(), extractRealmRoles(jwt).stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * This implementation does not add resource_access fields as roles
     */
    private static Collection<? extends GrantedAuthority> extractRealmRoles(final Jwt jwt) {
        // We suppress it as we know the structure of the token
        @SuppressWarnings("unchecked")
        Collection<String> userRoles = ((Map<String, List<String>>) jwt.getClaim("realm_access")).get("roles");
        if (userRoles != null)
            return userRoles
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        return Collections.emptySet();
    }
}
