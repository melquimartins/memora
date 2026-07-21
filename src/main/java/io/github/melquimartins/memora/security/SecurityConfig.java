package io.github.melquimartins.memora.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final SecurityFilter securityFilter;

  public SecurityConfig(SecurityFilter securityFilter) {
    this.securityFilter = securityFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity
  ) throws Exception {
    return httpSecurity
          .csrf(AbstractHttpConfigurer::disable)
          .sessionManagement(
                session ->
                      session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                      )
          )
          .addFilterBefore(
                securityFilter,
                UsernamePasswordAuthenticationFilter.class
          )
          .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                      HttpMethod.POST,
                      "/api/auth/sign-in"
                ).permitAll()
                .requestMatchers(
                      HttpMethod.POST,
                      "/api/auth/sign-up"
                ).permitAll()
                .anyRequest().authenticated()
          )
          .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
  ) {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
