package com.kibria.resilience4j.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/*
What is Bulkhead?

In the context of the Fault Tolerance mechanism, if we want to limit the number of concurrent requests,
 we can use Bulkhead as an aspect. Using Bulkhead, we can limit the number of concurrent requests within a
  particular period.


  Please note the difference between Bulkhead and Rate Limiting. Rate Limiter never talks
   about concurrent requests, but Bulkhead does. Rate Limiter talks about limiting number of requests within a
    particular period. Hence, using Bulkhead we can limit the number of concurrent requests.
     We can achieve this functionality easily with the help of annotation @Bulkhead without writing a
     specific code.
 */
@RestController
public class BulkheadController {

    Logger logger = LoggerFactory.getLogger(BulkheadController.class);

    @GetMapping("/getBulkheadMessage")
    @Bulkhead(name = "getMessageBH", fallbackMethod = "getMessageFallBack")
    public ResponseEntity<String> getMessage(){

        return ResponseEntity.ok().body("Message from Bulkhead getMessage()");
    }

    public ResponseEntity<String> getMessageFallBack(RequestNotPermitted exception) {

        logger.info("Bulkhead has applied, So no further calls are getting accepted");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests : No further request will be accepted. Plese try after sometime");
    }
}