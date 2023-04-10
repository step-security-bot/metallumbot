package com.github.errebenito.metallumbot;

import com.github.errebenito.metallumbot.command.CommandRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Main bot class.

 * @author rbenito
 *
 */

public class MetallumBot extends TelegramLongPollingBot {
  
  private static final String ERROR_MESSAGE = "Error sending message";

  private static final Logger LOGGER = LoggerFactory.getLogger(MetallumBot.class);

  private static final String USAGE = """
  Usage:
   
      /band Returns a random band
      /upcoming Returns a partial list of upcoming releases""";
  
  private static final String TOKEN = System.getenv("METALLUM_BOT_TOKEN");
  
  private static final String NAME = System.getenv("METALLUM_BOT_NAME");  
  
  private final CommandRunner runner = new CommandRunner();
  
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
        case "/band" -> {
          try {
            sendTextReply(update, runner.doBand());
          } catch (TelegramApiException e) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
        case "/upcoming" -> {
          try {
            sendTextReply(update, runner.doUpcoming());
          } catch (TelegramApiException e) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
        default -> {
          try {
            sendTextReply(update, USAGE);
          } catch (TelegramApiException e) {
            LOGGER.error(ERROR_MESSAGE);
          }
        }
      }
    }
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