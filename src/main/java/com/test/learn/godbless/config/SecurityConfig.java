package com.test.learn.godbless.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/fruits", "/fruits/**").hasRole("ROOT")
                .requestMatchers("/hi").hasAnyRole("USER", "ROOT")
                .requestMatchers("/").permitAll());
        http.formLogin(withDefaults());
        http.csrf(withDefaults());
        return http.build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails root = User.withDefaultPasswordEncoder().username("root").password("099175300").roles("ROOT")
                .build();
        UserDetails user = User.withDefaultPasswordEncoder().username("s22721").password("qttp").roles("USER")
                .build();
        return new InMemoryUserDetailsManager(root, user);
    }
}
