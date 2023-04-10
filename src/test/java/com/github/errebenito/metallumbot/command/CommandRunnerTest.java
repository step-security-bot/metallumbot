package com.github.errebenito.metallumbot.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the CommandRunner class.

 * @author rbenito
 *
 */
class CommandRunnerTest {

  private static CommandRunner runner;
  
  /**
   * Setup for the tests.

   * @throws MalformedURLException If there is an error trying to connect to the URLs.
   */
  @BeforeAll
  static void setUpBeforeClass() {
    runner = new CommandRunner();
  }

  @Test
  void whenDoBandShouldReturnBandLink() {
    final String result = runner.doBand();
    assertTrue(result.contains("https://www.metal-archives.com/band/view/id/"), "Return value was not a band link");
  }

  @Test
  void whenDoUpcomingShouldReturnAlbumLink() {
    final String result = runner.doUpcoming();
    assertTrue(result.contains("https://www.metal-archives.com/albums/"), "Return value did not contain an album link");
  }

}
