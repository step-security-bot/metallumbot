package com.github.errebenito.metallumbot.command;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.errebenito.metallumbot.connector.UrlConnector;
import com.github.errebenito.metallumbot.connector.UrlType;
import java.io.IOException;
import java.net.MalformedURLException;
import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the CommandRunner class.

 * @author rbenito
 *
 */
@ExtendWith(EasyMockExtension.class)
class CommandRunnerTest {

  @Mock
  UrlConnector mockConnector;
    
  /**
   * Setup for the tests.

   * @throws MalformedURLException In case of error trying to connect to the URLs.
   */

  @Test
  void whenDoBandShouldReturnBandLink() throws MalformedURLException {
    CommandRunner runner = new CommandRunner(new UrlConnector()
        .withUrl(UrlType.RANDOM_BAND.getUrl()));
    final String result = runner.doBand();
    assertTrue(result.contains("https://www.metal-archives.com/band/view/id/"), "Return value was not a band link");
  }

  @Test
  void whenDoUpcomingShouldReturnAlbumLink() throws MalformedURLException {
    CommandRunner runner = new CommandRunner(new UrlConnector()
        .withUrl(UrlType.UPCOMING_RELEASES.getUrl()));
    final String result = runner.doUpcoming();
    assertTrue(result.contains("https://www.metal-archives.com/albums/"), "Return value did not contain an album link");
  }
  
  @Test
  void whenDoBandWithBadUrlShouldThrowException() throws IOException {
    expect(mockConnector.connect()).andThrow(new IOException());
    replay(mockConnector);
    CommandRunner runner = new CommandRunner(mockConnector);
    runner.doBand();
    verify(mockConnector);  
  }
  
}
