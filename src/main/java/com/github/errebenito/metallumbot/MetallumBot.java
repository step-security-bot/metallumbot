package com.github.errebenito.metallumbot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.connector.UrlType;
import com.github.errebenito.metallumbot.model.UpcomingAlbums;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Bot class.

 * @author rbenito
 *
 */

public class MetallumBot extends TelegramLongPollingBot {
  private static final String LOCATION_HEADER = "Location";

  private static final String ERROR_MESSAGE = "Error sending message";

  private static final Logger LOGGER = LoggerFactory.getLogger(MetallumBot.class);

  private static final String USAGE = """
  Usage:
   
      /band Returns a random band
      /upcoming Returns a partial list of upcoming releases""";
  
  private static final String TOKEN = System.getenv("METALLUM_BOT_TOKEN");
  private static final String NAME = System.getenv("METALLUM_BOT_NAME");  
  
  
  
  /**
   * Constructor.
   */
  public MetallumBot() {
    super(TOKEN);
  }
  
  /**
   * Method for receiving messages.

   * @param update Contains a message from the user.
   */
  @Override
  public void onUpdateReceived(final Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      final String messageText = update.getMessage().getText();
      switch (messageText) {
        case "/start" -> doStart(update);
        case "/band" -> doBand(update);
        case "/upcoming" -> doUpcoming(update);
        default -> doStart(update);
      }
    }
  }
  
  private void doStart(final Update update) {
    try {
      sendTextReply(update, USAGE);
    } catch (TelegramApiException e) {
      LOGGER.error(ERROR_MESSAGE);
    }
  }
  
  private void doBand(final Update update) {
    String bandUrl;
    try {
      bandUrl = new UrlConnector().withUrl(UrlType.RANDOM_BAND)
          .connect().getHeaderField(LOCATION_HEADER);
      sendTextReply(update, bandUrl);
    } catch (IOException | TelegramApiException e) {
      LOGGER.error(ERROR_MESSAGE);
    }
  }
  
  private void doUpcoming(final Update update) {
    try {
      final HttpURLConnection connection = 
          new UrlConnector().withUrl(UrlType.UPCOMING_RELEASES).connect();
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
      final UpcomingAlbums albums = objectMapper.readValue(getInput(connection), 
          UpcomingAlbums.class); 
      sendTextReply(update, albums.toString());      
    } catch (IOException | TelegramApiException e) {
      LOGGER.error(ERROR_MESSAGE);
    }
  }

  @SuppressFBWarnings
  private InputStream getInput(final HttpURLConnection connection) throws IOException {
    String result;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
        connection.getURL().openStream())))) {
      result = reader.lines().collect(Collectors.joining("\n"));
    }
    result = result.replace(": ,", ": 0,");
    return new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
  }
    
  private void sendTextReply(final Update update, final String text) throws TelegramApiException {
    final SendMessage message = new SendMessage();
    message.setChatId(update.getMessage().getChatId());
    message.setText(text);
    execute(message);
  }
  
  /**
   * Returns the bot name which was specified during registration.

   * @return The bot name
   */
  @Override
  public String getBotUsername() {
    return NAME;
  }

  /**
   * Main method.

   * @param args The arguments.
   */
  public static void main(final String[] args) {

    TelegramBotsApi botsApi;

    try {
      botsApi = new TelegramBotsApi(DefaultBotSession.class);
      botsApi.registerBot(new MetallumBot());
    } catch (TelegramApiException e) {
      LOGGER.error("Error setting up and registering bot");
    }
  }
}