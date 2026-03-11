package com.revhire.revhirep2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/login",
                        "/register",
                        "/",
                        "/landing",
                        "/forgot-password",
                        "/reset-password"
                ).permitAll()
                .requestMatchers("/jobseeker/**").hasAuthority("ROLE_JOB_SEEKER")
                .requestMatchers("/employer/**").hasAuthority("ROLE_EMPLOYER")
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler(successHandler)   // ✅ USE COMPONENT
                    .permitAll()
            )

            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/logout-success")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

    // ✅ KEEP password encoder here (safe)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}