package com.samcox.ranker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@SpringBootApplication
public class RankerApplication {

  /*
  @RequestMapping("/resource")
  public Map<String,Object> home() {
    Map<String,Object> model = new HashMap<String,Object>();
    model.put("id", UUID.randomUUID().toString());
    model.put("content", "Hello World");
    return model;
  }
*/

  /*
  @Configuration
  public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
        .authorizeHttpRequests((authorize) -> authorize
          .requestMatchers("/index.html", "/", "/home,", "/login").permitAll()
          .anyRequest().authenticated()
        );
      return http.build();
    }
  }
   */

	public static void main(String[] args) {
		SpringApplication.run(RankerApplication.class, args);
	}

  @Bean
  CommandLineRunner init(UserRepository userRepository) {
    return args -> {
      Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(username -> {
        User user = new User(username, username, "USER");
        userRepository.save(user);
      });
      userRepository.findAll().forEach(System.out::println);
    };
  }

}
