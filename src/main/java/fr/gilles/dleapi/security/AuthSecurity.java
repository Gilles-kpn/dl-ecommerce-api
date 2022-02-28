package fr.gilles.dleapi.security;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import fr.gilles.dleapi.security.request.RequestFilter;
import fr.gilles.dleapi.services.user.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
public class AuthSecurity extends WebSecurityConfigurerAdapter {
    private final AuthUserService authUserService;
    private final RequestFilter requestFilter;
    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/"
    };

    private static  final String[] AUTH_GET_ROUTES = {
            "/auth/login",
            "/auth/register",
            "/auth/activate/*",
            "/category/**",
            "/product/**"
    };

    private static  final  String[] AUTH_POST_ROUTES = {
            "/auth/login",
            "/auth/register",
    };


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public RequestFilter getRequestFilter(){
        return requestFilter;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_MANAGER > ROLE_ADMIN \n ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "daqeiaxg4",
                "api_key", "746416169816273",
                "api_secret", "tv8AakEEWJ2w_PLzmeY0ScL8kpc",
                "secure", true)
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        http.cors().configurationSource(request -> corsConfiguration.applyPermitDefaultValues());
        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
            .httpBasic()
                .disable()
            .authorizeRequests()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .antMatchers(HttpMethod.POST, AUTH_POST_ROUTES )
                .permitAll()
                .antMatchers(HttpMethod.GET, AUTH_GET_ROUTES)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
