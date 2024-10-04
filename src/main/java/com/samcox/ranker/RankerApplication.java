package com.samcox.ranker;

import com.samcox.ranker.media.EntryAddRequest;
import com.samcox.ranker.media.FilmDTO;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaListService;
import com.samcox.ranker.ranking.NumberedRanking;
import com.samcox.ranker.ranking.NumberedRankingDTO;
import com.samcox.ranker.ranking.NumberedRankingService;
import com.samcox.ranker.tmdb.*;
import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserService;
import jakarta.transaction.Transactional;
import jdk.swing.interop.SwingInterOpUtils;
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

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class RankerApplication implements CommandLineRunner {

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private NumberedRankingService numberedRankingService;

  @Autowired
  private TmdbService tmdbService;

  @Autowired
  private MediaListService mediaListService;

  public static void main(String[] args) {
		SpringApplication.run(RankerApplication.class, args);
	}

  @Override
  @Transactional
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
    ranking.setDescription("A ranking containing movies");
    ranking.setMediaType("FILM");
    numberedRankingService.createNumberedRanking(ranking);

    NumberedRankingDTO ranking2 = new NumberedRankingDTO();
    ranking2.setUserDTO(userDTO);
    ranking2.setTitle("This is test ranking 2");
    ranking2.setDescription("A ranking containing TV Shows");
    ranking2.setMediaType("TV_SHOW");
    numberedRankingService.createNumberedRanking(ranking2);

    List<NumberedRanking> numberedRankings = numberedRankingService.getAllNumberedRankingsByUser(userDTO.getId());
    for(NumberedRanking numberedRanking: numberedRankings) {
      System.out.println("Ranking Id: " + numberedRanking.getId());
    }

    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(1L,userDTO.getId());
    MediaList mediaList1 = mediaListService.getMediaListByNumberedRankingAndUser(2L,userDTO.getId());

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(1);

    EntryAddRequest entryAddRequest1 = new EntryAddRequest();
    entryAddRequest1.setTmdbId(4638L);
    entryAddRequest1.setRanking(2);

    EntryAddRequest entryAddRequest2 = new EntryAddRequest();
    entryAddRequest2.setTmdbId(7345L);
    entryAddRequest2.setRanking(3);

    //Film entries for ranking 1


    mediaListService.addEntryToList(entryAddRequest, 1L);
    mediaListService.addEntryToList(entryAddRequest1, 1L);
    mediaListService.addEntryToList(entryAddRequest2, 1L);

    System.out.println(mediaList.getEntries());

    mediaListService.moveEntryInList(1L, 1, 2);

    System.out.println(mediaList.getEntries());

    mediaListService.moveEntryInList(1L, 1, 2);

    System.out.println(mediaList.getEntries());



    //TVShow entries for ranking 2

    EntryAddRequest entryAddRequest3 = new EntryAddRequest();
    entryAddRequest3.setTmdbId(1399L);
    entryAddRequest3.setRanking(1);

    EntryAddRequest entryAddRequest4 = new EntryAddRequest();
    entryAddRequest4.setTmdbId(1396L);
    entryAddRequest4.setRanking(2);

    EntryAddRequest entryAddRequest5 = new EntryAddRequest();
    entryAddRequest5.setTmdbId(40008L);
    entryAddRequest5.setRanking(3);

    mediaListService.addEntryToList(entryAddRequest3, 2L);
    mediaListService.addEntryToList(entryAddRequest4, 2L);
    mediaListService.addEntryToList(entryAddRequest5, 2L);

    //System.out.println(mediaList1.getEntries());

    SecurityContextHolder.clearContext();
  }
}
