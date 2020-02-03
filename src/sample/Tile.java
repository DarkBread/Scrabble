package sample;

public class Tile {

  private static final int EMPTY_VALUE = 0;
  private char letter;


  public char getLetter() {
    return letter;
  }

  public void setLetter(char letter) {
    this.letter = letter;
  }

  public void setEmpty() {
    this.letter = EMPTY_VALUE;
  }

  public boolean isEmpty() {
    return this.letter == EMPTY_VALUE;
  }
}
