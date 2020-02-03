package sample;

public class Player {

  private String name;
  private int score;
  private Frame frame;

  public Player(String name) {
    this.name = name;
    frame = new Frame();
    frame.fillFrameWithTiles();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void increaseScoreBy(int points) {
    score += points;
  }

  public Frame getFrame() {
    return frame;
  }
}
