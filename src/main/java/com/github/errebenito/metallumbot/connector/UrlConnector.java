package com.github.errebenito.metallumbot.connector;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Manages connections to the metal-archives site.

 * @author rbenito
 *
 */
public class UrlConnector {
  
  private static final int TIMEOUT = 5000;
  
  private static final String RANDOM_BAND_URL = "https://www.metal-archives.com/band/random";
  
  private static final String NEW_RELEASES_URL = "https://www.metal-archives.com/release/ajax-upcoming/json";
  
  private URL url;
  
  private Map<UrlType, String> validUrls;
  
  /**
   * Constructor.
   */
  public UrlConnector() {
    new UrlConnector(null);
    addUrls();
  }
  
  private UrlConnector(final URL url) {
    this.url = url;   
  }
  
  private void addUrls() {
    this.validUrls = new ConcurrentHashMap<>();
    this.validUrls.put(UrlType.RANDOM_BAND, RANDOM_BAND_URL);
    this.validUrls.put(UrlType.UPCOMING_RELEASES, NEW_RELEASES_URL);
  }
  
  /**
   * Specifies the URL associated to the connector.

   * @param type The type of URL to connect to.
   * @return an instance of UrlConnector
   * @throws MalformedURLException When the URL is not valid
   */
  public UrlConnector withUrl(final UrlType type) throws MalformedURLException {
    this.url = new URL(this.validUrls.get(type));    
    return new UrlConnector(url);
  }
  
  /**
   * Connects to the URL associated to the connector.

   * @return An instance of HttpURLConnection.
   * @throws IOException When an error occurs attempting to connect.
   */
  @SuppressFBWarnings
  public HttpURLConnection connect() throws IOException {
    // The URL comes from a list of valid URLs and therefore it is safe to connect.
    final HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
    connection.setReadTimeout(TIMEOUT);
    connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
    connection.addRequestProperty("User-Agent", "Mozilla");
    connection.addRequestProperty("Referer", "google.com");
    connection.setInstanceFollowRedirects(false);
    return connection;
  }
  
  /**
   * Reads the upcoming albums JSON.

   * @return A ByteArrayInputStream with the contents of the JSON.
   * @throws IOException When an error occurs while reading the JSON.
   */
  @SuppressFBWarnings
  public InputStream readUpcomingAlbumsJson() throws IOException {
    String result;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
        this.url.openStream())))) {
      result = reader.lines().collect(Collectors.joining("\n"));
    }
    result = result.replace(": ,", ": 0,");
    return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
  }
}
