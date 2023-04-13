package com.github.errebenito.metallumbot;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Unit tests for the MetallumBot class.

 * @author rbenito
 *
 */
class MetallumBotTest {
  
  @ParameterizedTest
  @ValueSource(strings = {"/start", "/band", "/upcoming"})
  void testOnUpdateReceived(final String command) {
    final Message message = new Message();
    message.setText(command);
    final Chat chat = new Chat();
    chat.setId(1L);
    message.setChat(chat);
    final Update updateMock = createMock(Update.class);
    expect(updateMock.hasMessage()).andReturn(true);
    expect(updateMock.getMessage()).andReturn(message);
    expectLastCall().times(3);
    replay(updateMock);
    final MetallumBot bot = new MetallumBot();
    bot.onUpdateReceived(updateMock);
    verify(updateMock);
  }

  @Test
  void testGetBotUsername() {
    final MetallumBot bot = new MetallumBot();
    assertEquals(System.getenv("METALLUM_BOT_NAME"),
        bot.getBotUsername(), "Bot returned incorrect name");
  }

}
