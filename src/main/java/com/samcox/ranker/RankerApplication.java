package com.samcox.ranker;

import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RankerApplication implements CommandLineRunner {

  @Autowired
  private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(RankerApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {
    // Register a default user at startup
    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("steve");
    userCredentials.setPassword("Stevesteve1!");
    userService.createUser(userCredentials);
    UserCredentials userCredentials2 = new UserCredentials();
    userCredentials2.setUsername("dave");
    userCredentials2.setPassword("Davedave1!");
    userService.createUser(userCredentials2);
    UserCredentials userCredentials3 = new UserCredentials();
    userCredentials3.setUsername("john");
    userCredentials3.setPassword("Johnjohn1!");
    userService.createUser(userCredentials3);
    System.out.println("Default users registered successfully");
  }
}
