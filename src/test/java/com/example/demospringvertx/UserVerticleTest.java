package com.example.demospringvertx;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UserVerticleTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Test
    public void getAllUsers() throws InterruptedException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + applicationConfiguration.httpPort() + "/api/user", String.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
