 package com.g02.handyShare;

 import org.junit.jupiter.api.Test;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.boot.test.web.server.LocalServerPort;

 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 class HandyShareApplicationTests {
     @LocalServerPort
     private int port;
 }
