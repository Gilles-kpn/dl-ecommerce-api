package fr.gilles.auth.security.request;

import fr.gilles.auth.security.jwt.JWTUtils;
import fr.gilles.auth.services.user.AuthUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final AuthUserService authUserService;
    public static  final String AUTHORIZATION_FIELD = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (hasValidAuthorization(request)){
            UserDetails userDetails = authUserService.loadUserByUsername(jwtUtils.extractUserName(extractToken(request)));
            if (jwtUtils.isValidToken(extractToken(request), userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
    private String extractToken(HttpServletRequest request){
        return  request.getHeader(AUTHORIZATION_FIELD).replace(JWTUtils.PREFIX, "");
    }
    private boolean hasValidAuthorization(HttpServletRequest request){
        return  request.getHeader(AUTHORIZATION_FIELD) != null && request.getHeader(AUTHORIZATION_FIELD).startsWith(JWTUtils.PREFIX);
    }
}
