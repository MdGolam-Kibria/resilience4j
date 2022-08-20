package com.kibria.resilience4j.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/*
What is Rate Limiting?
Rate Limiter limits the number of requests for a given period.
Let’s assume that we want to limit the number of requests on a Rest API and fix it for a particular duration.
There are various reasons to limit the number of requests that an API can handle,
such as protect the resources from spammers, minimize the overhead,
meet a service level agreement and many others. Undoubtedly,
we can achieve this functionality with the help of annotation @RateLimiter provided by Resilience4j
without writing a code explicitly.
 */
@RestController
public class RateLimiterExample {
    Logger logger = LoggerFactory.getLogger(RateLimiterExample.class);

    @GetMapping("/getMessage")
    @RateLimiter(name = "getMessageRateLimit", fallbackMethod = "getMessageFallBack")
    public ResponseEntity<String> getMessage() {

        return ResponseEntity.ok().body("Message from getMessage() rate limiter example");
    }

    public ResponseEntity<String> getMessageFallBack(RequestNotPermitted exception) {

        logger.info("Rate limit has applied, So no further calls are getting accepted");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests : No further request will be accepted. Please try after sometime");
    }
}
