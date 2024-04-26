package com.example.internetforum.security;

import com.example.internetforum.models.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*", "Authorization");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET,"api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/users").hasAuthority(UserRole.ADMINISTRATOR.toString().toLowerCase())
                        .requestMatchers(HttpMethod.PUT, "api/users/**").hasAuthority(UserRole.ADMINISTRATOR.toString().toLowerCase())
                        .requestMatchers(HttpMethod.GET, "api/forums/find-all-forums").authenticated()
                        .requestMatchers(HttpMethod.GET, "api/forums/*/find-forum").authenticated()
                        .requestMatchers(HttpMethod.GET, "api/forums/*/find-all-comments").authenticated()
                        .requestMatchers(HttpMethod.POST,"api/forums/add-comment").hasAuthority("add")
                        .requestMatchers(HttpMethod.PUT, "api/forums/edit-comment").hasAuthority("edit")
                        .requestMatchers(HttpMethod.DELETE,"api/forums/*/delete-comment").hasAuthority("delete")
                        .requestMatchers(HttpMethod.GET,"api/forums/comments/not-approved-comments")
                        .hasAnyAuthority(UserRole.ADMINISTRATOR.toString().toLowerCase(), UserRole.MODERATOR.toString().toLowerCase())
                        .requestMatchers(HttpMethod.PUT,"api/forums/*/accept-comment")
                        .hasAnyAuthority(UserRole.ADMINISTRATOR.toString().toLowerCase(), UserRole.MODERATOR.toString().toLowerCase())
                        .requestMatchers(HttpMethod.PUT,"api/forums/comments/edi-comment")
                        .hasAnyAuthority(UserRole.ADMINISTRATOR.toString().toLowerCase(), UserRole.MODERATOR.toString().toLowerCase())
                        .requestMatchers(HttpMethod.DELETE,"api/forums/*/decline-comment")
                        .hasAnyAuthority(UserRole.ADMINISTRATOR.toString().toLowerCase(), UserRole.MODERATOR.toString().toLowerCase())
                        .requestMatchers(HttpMethod.POST, "api/users/logging").permitAll()
                        .anyRequest()
                        .denyAll())
                .sessionManagement((manager) -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
