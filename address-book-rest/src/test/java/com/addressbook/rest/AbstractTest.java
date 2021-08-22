package com.addressbook.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.Getter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

  @LocalServerPort
  @Getter
  protected int port;

  @Autowired
  @Getter
  protected TestRestTemplate restTemplate;

  @Getter
  private ObjectMapper objectMapper = new ObjectMapper();

  @Getter
  protected String baseUrl;

  @Getter
  protected HttpHeaders headers;


  @BeforeEach
  public void before() {
    baseUrl = "http://localhost:" + port;
    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
  }


  protected String getUrl(String path) {
    return baseUrl + path;
  }

}
