package com.example.study_spring_security.controller;

import com.example.study_spring_security.dto.CreateTweetDto;
import com.example.study_spring_security.entities.Tweet;
import com.example.study_spring_security.repository.TweetRepository;
import com.example.study_spring_security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TweetController {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweets")
    @Transactional
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto createTweetDto,
                                            JwtAuthenticationToken token) { // permite identificar qual usuário esta vindo na requisição
    var user = userRepository.findById(UUID.fromString(token.getName()));
    var tweet = new Tweet();
    tweet.setUser(user.get());
    tweet.setContent(createTweetDto.content());

    tweetRepository.save(tweet);
    return ResponseEntity.ok().build();

    }

}
