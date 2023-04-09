package com.github.errebenito.metallumbot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

public class MetallumBot extends TelegramLongPollingBot { // NOPMD
  private static final Logger LOGGER = LoggerFactory.getLogger(MetallumBot.class);

  private static final String USAGE = """
  Usage:
   
      /band Returns a random band""";

  private static final String RANDOM_BAND_URL = "https://www.metal-archives.com/band/random";
  
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
      if ("/start".equals(messageText)) {
        final SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(USAGE);
        try {
          execute(message);
        } catch (TelegramApiException e) {
          LOGGER.error("Error sending message");
        }
      } else if ("/band".equals(messageText)) {
        try {
          final String bandUrl = setupConnection().getHeaderField("Location");
          final SendMessage message = new SendMessage();
          message.setChatId(update.getMessage().getChatId());
          message.setText(bandUrl);
          execute(message);
        } catch (IOException | TelegramApiException e) {
          LOGGER.error("Error sending message");
        }
      }
    }
  }
  
  private HttpURLConnection setupConnection() throws IOException {
    final URL obj = new URL(RANDOM_BAND_URL);
    final HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
    connection.setReadTimeout(5000);
    connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
    connection.addRequestProperty("User-Agent", "Mozilla");
    connection.addRequestProperty("Referer", "google.com");
    connection.setInstanceFollowRedirects(false);
    return connection;
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
   * Returns the bot token for communicating with the Telegram server.

   * @return the bot token
   */
  @Override
  public String getBotToken() {
    return TOKEN;
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