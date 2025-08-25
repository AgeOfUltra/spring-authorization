package com.authorization.springauthorization.config;

import com.authorization.springauthorization.entity.Permissions;
import com.authorization.springauthorization.filter.JwtAuthenticationFilter;
import com.authorization.springauthorization.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthenticationFilter  jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers( "/h2-console/**","/api/**")
        )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/h2-console/**","api/authenticate").permitAll()
                        .requestMatchers("/app/weather").hasAuthority(Permissions.WEATHER_READ.name())
                        .requestMatchers(HttpMethod.GET,"/api/weather/health","/api/weather/all","/api/weather/forCity").hasAuthority(Permissions.WEATHER_READ.name())
                        .requestMatchers(HttpMethod.DELETE,"/api/weather/deleteCity").hasAuthority(Permissions.WEATHER_DELETE.name())
                        .requestMatchers(HttpMethod.PUT,"/api/weather/**").hasAuthority(Permissions.WEATHER_WRITE.name())
                )

//                .httpBasic(Customizer.withDefaults()); // this part will remove the basic authentication.

        // now we need to add our jwt filter before username-password-Authentication-filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CustomUserDetailsService  customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService customUserDetailsService,PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }
}
