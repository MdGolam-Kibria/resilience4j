package com.kibria.resilience4j.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.retry.annotation.Retry;
/*
What is Retry?
Suppose Microservice ‘A’  depends on another Microservice ‘B’. Let’s assume Microservice ‘B’ is a faulty service and its success
rate is only upto 50-60%. However, fault may be due to any reason, such as service is unavailable,
buggy service that sometimes responds and sometimes not, or an intermittent network failure etc. However, in this case,
if Microservice ‘A’ retries to send request 2 to 3 times, the chances of getting response increases. Obviously,
we can achieve this functionality with the help of annotation @Retry provided by Resilience4j without writing a code explicitly.

Here, we have to implement a Retry mechanism in Microservice ‘A’. We will call Microservice ‘A’ as
Fault Tolerant as it is participating in tolerating the fault. However, Retry will take place only on a failure not on a success.
By default retry happens 3 times. Moreover, we can configure how many times to retry as per our requirement.
 */
@RestController
public class RetryExample {
    Logger logger = LoggerFactory.getLogger(RetryExample.class);
    RestTemplate restTemplate= new RestTemplate();

    @GetMapping("/getInvoiceInfo")
    @Retry(name = "getInvoiceRetry", fallbackMethod = "getInvoiceFallback")
    public String getInvoice() {
        logger.info("getInvoice() call starts here");
        ResponseEntity<String> entity= restTemplate.getForEntity("http://localhost:8080/invoice/rest/find/2", String.class);
        logger.info("Response :" + entity.getStatusCode());
        return entity.getBody();
    }

    public String getInvoiceFallback(Exception e) {
        logger.info("---RESPONSE FROM FALLBACK METHOD---");
        return "SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME !!!";
    }
}
