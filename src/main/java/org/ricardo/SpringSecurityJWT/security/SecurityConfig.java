package org.ricardo.SpringSecurityJWT.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import org.ricardo.SpringSecurityJWT.jwt.JwtUtils;
import org.ricardo.SpringSecurityJWT.security.filters.JwtAuthenticationFilter;
import org.ricardo.SpringSecurityJWT.security.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {
	
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired 
	JwtAuthorizationFilter authorizationFilter;
	

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
    	JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
    	jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    	jwtAuthenticationFilter.setFilterProcessesUrl("/login");
    	
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/hello").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("santiago")
//            .password(passwordEncoder().encode("1234")) 
//            .roles("USER")
//            .build());
//        return manager;
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) 
            throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
        		.userDetailsService(userDetailsService)
        		.passwordEncoder(passwordEncoder)
        		.and().build();
    }
    
}

