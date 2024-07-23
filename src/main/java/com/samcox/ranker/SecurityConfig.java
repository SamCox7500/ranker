package com.samcox.ranker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests((authorize) -> authorize
        //use .requestMatchers("/**").permitAll()
        //.requestMatchers(HttpMethod.POST, "/users").permitAll()
        .requestMatchers("/login").permitAll()
        .anyRequest().authenticated()
      )
      .httpBasic(Customizer.withDefaults())
      .formLogin(Customizer.withDefaults())
      .csrf((csrf) -> csrf.disable());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(customUserDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(authenticationProvider);
  }
  /*
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails userDetails = User.builder()
      .username("user")
      .password(passwordEncoder().encode("password"))
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(userDetails);
  }
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
