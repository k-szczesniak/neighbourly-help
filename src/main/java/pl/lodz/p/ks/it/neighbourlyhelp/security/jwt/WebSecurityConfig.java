package pl.lodz.p.ks.it.neighbourlyhelp.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.service.UserDetailsServiceImpl;
import pl.lodz.p.ks.it.neighbourlyhelp.exception.handler.FilterChainExceptionHandler;

import javax.crypto.SecretKey;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final FilterChainExceptionHandler filterChainExceptionHandler;

    private final UserDetailsServiceImpl userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtil jwtUtil;

    private final SecretKey secretKey;

    private final TokenVerifier tokenVerifier;

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/error",
            // loginPage
            "/auth/login"
            // other public endpoints of your API may be appended to this array
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(), jwtUtil, secretKey);
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");

        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/api/token/refresh/**").permitAll()
//                .antMatchers("/api/ping").hasAnyAuthority("CLIENT", "ADMIN")
//                .anyRequest().authenticated()
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterAfter(new CustomAuthorizationFilter(jwtUtil, tokenVerifier), CustomAuthenticationFilter.class)
                .addFilterBefore(filterChainExceptionHandler, CustomAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}