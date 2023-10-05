package com.LondonApiTest.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.LondonApiTest.SpringConfig;
import com.LondonApiTest.service.LondonService;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

@WireMockTest
class LondonControllerTest {

  private LondonController londonController;

  @Test
  void getAllLondonPeople(WireMockRuntimeInfo wmRuntimeInfo) {
    final SpringConfig springConfig = mock(SpringConfig.class);
    when(springConfig.getBaseApi()).thenReturn(wmRuntimeInfo.getHttpBaseUrl());

    // TODO: mock/intercept the Wiremock GET...

    londonController = new LondonController(springConfig, WebClient.builder(), new LondonService());
    londonController.getAllLondonPeople();
  }

  @Test
  void mapJsonToPersonRequest() {
  }

  @Test
  void getAllUsers() {
  }

  @Test
  void getCity() {
  }
}