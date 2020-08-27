package capstone.backend.api.security.jwt;

import capstone.backend.api.service.impl.security.UserDetailsServiceImpl;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    private JwtUtils jwtUtils;

    private UserDetailsServiceImpl userDetailsService;

    private String parseJwt(HttpServletRequest req){
        String headerAuth = req.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("lidi ") ){
            return  headerAuth.substring(5);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = parseJwt(req);
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e){
            logger.error("Cannot set user authentication!",e);
        }

        filterChain.doFilter(req,res);
    }
}
