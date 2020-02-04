package sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

  //will be needed to implement rest of the tests for Player class and add more tests later
  @org.junit.jupiter.api.Test
  void getName() {
    Player player = new Player("Bob");
    assertEquals("Bob", player.getName());
  }

  @org.junit.jupiter.api.Test
  void increaseScoreBy() {
    Player player = new Player("Bob");
    player.increaseScoreBy(15);
    assertEquals(15, player.getScore());
  }

}