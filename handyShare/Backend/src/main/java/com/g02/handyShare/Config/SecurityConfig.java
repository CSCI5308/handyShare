package com.g02.handyShare.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.g02.handyShare.Config.Jwt.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// <<<<<<< feature/product-page-updates
//         return http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/signup", "/api/v1/all/genToken").permitAll()  // Permit these endpoints
//                         .requestMatchers("/api/v1/all/**").permitAll() // Bypass authentication for all /api/v1/all/** endpoints
//                         .requestMatchers("/api/v1/admin/**").hasAuthority("admin")  // Only accessible by users with ADMIN role
//                         .requestMatchers("/api/v1/user/**").hasAuthority("user")    // Only accessible by users with USER role
//                         .anyRequest().authenticated()  // All other endpoints need authentication
//                 )
//                 .httpBasic(Customizer.withDefaults())
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .authenticationProvider(authenticationProvider())
//                 .build();
// =======
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/api/v1/all/**", "/genToken").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("admin")  // Only accessible by users with ADMIN role
                        .requestMatchers("/user/**").hasAnyAuthority("user", "admin")  // Only accessible by users with USER or ADMIN roles
                        .anyRequest().authenticated()  // All other endpoints need authentication
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(csrf -> csrf.disable())
                .formLogin().disable()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
