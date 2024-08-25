package com.samcox.ranker;

import com.samcox.ranker.ranking.NumberedRankingDTO;
import com.samcox.ranker.ranking.NumberedRankingService;
import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;

@SpringBootApplication
public class RankerApplication implements CommandLineRunner {

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private NumberedRankingService numberedRankingService;

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

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      userCredentials.getUsername(), userCredentials.getPassword());
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);


    UserDTO userDTO = new UserDTO();
    userDTO.setId(userService.getUserByUsername("steve").getId());
    userDTO.setUsername("steve");
    NumberedRankingDTO ranking = new NumberedRankingDTO();
    ranking.setUserDTO(userDTO);
    ranking.setTitle("This is test ranking 1");
    ranking.setDescription("A test ranking. DAWDWADADWADAWDWADAWDADWADAWDAWD AWD DA WD AWD AWD DWA WDA D WAD A");
    numberedRankingService.createNumberedRanking(ranking);

    SecurityContextHolder.clearContext();
  }
}
