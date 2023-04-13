package com.github.errebenito.metallumbot.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the UrlConnector class. 

 * @author rbenito
 *
 */
class UrlConnectorTest { 

  private static UrlConnector connector;
  
  /**
   * Setup for the tests.

   * @throws MalformedURLException In case of error trying to connect to the URLs.
   */
  @BeforeAll
  static void setup() throws MalformedURLException {
    connector = new UrlConnector().withUrl(UrlType.RANDOM_BAND.getUrl());
  }

  @Test
  void whenRandomBandUrlShouldReturnCorrectUrl() throws IOException {
    assertEquals(UrlType.RANDOM_BAND.getUrl(), 
        connector.connect().getURL(), "URL did not match");
  }
 
  @Test
  void whenUpcomingReleasesUrlShouldReturnCorrectUrl() throws IOException {
    connector = new UrlConnector().withUrl(UrlType.UPCOMING_RELEASES.getUrl());
    assertEquals(UrlType.UPCOMING_RELEASES.getUrl(), 
        connector.connect().getURL(), "URL did not match");
  }

  
  @Test
  void whenConnectShouldHaveTimeout() throws IOException {
    final int timeout = connector.connect().getReadTimeout();
    assertEquals(5000, timeout, "Timeout did not match the expected value");
  }

  @Test
  void whenConnectShouldHaveLanguageRequestProperty() throws IOException {
    final String language = connector.connect().getRequestProperty("Accept-Language");
    assertEquals("en-US,en;q=0.8", language, "Language did not match the expected value");
  }

  @Test
  void whenConnectShouldHaveUserAgentRequestProperty() throws IOException {
    final String agent = connector.connect().getRequestProperty("User-Agent");
    assertEquals("Mozilla", agent, "User agent did not match the expected value");
  }
  
  @Test
  void whenConnectShouldHaveUserRefererProperty() throws IOException {
    final String referer = connector.connect().getRequestProperty("Referer");
    assertEquals("google.com", referer, "Referer did not match the expected value");
  }
  
}
