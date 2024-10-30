package com.example.study_spring_security.controller;

import com.example.study_spring_security.dto.CreateTweetDto;
import com.example.study_spring_security.dto.FeedDto;
import com.example.study_spring_security.dto.FeedItemDto;
import com.example.study_spring_security.entities.Role;
import com.example.study_spring_security.entities.Tweet;
import com.example.study_spring_security.repository.TweetRepository;
import com.example.study_spring_security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.patterns.BindingTypePattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @DeleteMapping("tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId,
                                            JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));
        // Busca o tweet no banco de dados
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRole().stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        // Verifica se o usuário que está tentando deletar o tweet é o mesmo que o criou
        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.deleteById(tweetId);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        var tweets = tweetRepository.findAll(
                        PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                .map(tweet ->
                        new FeedItemDto(
                                tweet.getRoleId(),
                                tweet.getContent(),
                                tweet.getUser().getUsername())
                );

        return ResponseEntity.ok(new FeedDto(
                tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()));
    }

}
