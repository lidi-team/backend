package capstone.backend.api.utils.security;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.security.TokenResponseInfo;
import capstone.backend.api.service.impl.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final CommonProperties commonProperties;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + commonProperties.getExpiration());

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, commonProperties.getSecret())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(commonProperties.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(commonProperties.getSecret())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature!", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token!", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired!", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported!", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty!", e.getMessage());
        }
        return false;
    }

    public TokenResponseInfo generateTokenResponseInfo(String email, String password, AuthenticationManager authenticationManager) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        return TokenResponseInfo.builder()
                .jwtToken(jwt)
                .user(new TokenResponseInfo().userResponse(userDetails, roles)).build();
    }

}
