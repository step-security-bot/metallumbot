package com.github.errebenito.metallumbot.command;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.connector.UrlType;
import com.github.errebenito.metallumbot.model.UpcomingAlbums;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runner for the various commands supported by the bot.

 * @author rbenito
 *
 */
public class CommandRunner {
  private static final String LOCATION_HEADER = "Location";
  private static final Logger LOGGER = LoggerFactory.getLogger(CommandRunner.class);
  private static final String ERROR_MESSAGE = "Error retrieving data";
  
  /**
   * Retrieves the URL to a random band.

   * @return A string representation of the URL.
   */
  public String doBand() {
    String result = "";
    try {
      result = new UrlConnector().withUrl(UrlType.RANDOM_BAND)
          .connect().getHeaderField(LOCATION_HEADER);
    } catch (IOException e) {
      LOGGER.error(ERROR_MESSAGE);
    }
    return result;
  }
  
  /**
   * Retrieves the first 10 upcoming albums.

   * @return A string representation of links to the first 10 upcoming albums.
   */
  public String doUpcoming() {
    String result = "";
    try {
      final UrlConnector connector = new UrlConnector().withUrl(UrlType.UPCOMING_RELEASES);
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
      final UpcomingAlbums albums = objectMapper.readValue(connector.readUpcomingAlbumsJson(), 
          UpcomingAlbums.class); 
      result = albums.toString();
    } catch (IOException e) {
      LOGGER.error(ERROR_MESSAGE);
    }
    return result;
  }
}
