package com.samcox.ranker;

import org.springframework.beans.factory.annotation.Autowired;
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

@SpringBootApplication
public class RankerApplication implements CommandLineRunner {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

	public static void main(String[] args) {
		SpringApplication.run(RankerApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {
    // Register a default user at startup
    customUserDetailsService.registerUser("steve", "steve");
    customUserDetailsService.registerUser("dave", "dave");
    System.out.println("Default user registered successfully");
  }
}
