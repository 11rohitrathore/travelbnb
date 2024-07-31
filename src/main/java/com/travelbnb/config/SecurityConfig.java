package com.travelbnb.config;

import com.auth0.jwt.JWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        http.csrf().disable().cors().disable();
        http.authorizeHttpRequests().anyRequest().permitAll();
/*        http.authorizeHttpRequests()
                .requestMatchers("/api/v1/user/login","/api/v1/user/createUser").permitAll().and().authorizeHttpRequests()
                .requestMatchers("/api/v1/countries/addCountry").hasRole("ADMIN")
                .requestMatchers("/api/v1/photos/upload").hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated();
*/
        return http.build();
    }
}
