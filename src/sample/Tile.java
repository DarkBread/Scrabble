package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;


public class Tile extends Label {

  private static final int EMPTY_VALUE = 0;
  private char letter;

  public Tile(char letter) {
    this();
    setLetter(letter);
  }

  public Tile() {
    setStyle("-fx-border-color: #ffb144;-fx-font-weight: bold");
    setPrefSize(40, 40);
    //setLetter(((char) ('A' + (int) (Math.random() * 24))));
    setAlignment(Pos.CENTER);
  }

  public char getLetter() {
    return letter;
  }

  public void setLetter(char letter) {
    this.letter = letter;
    setText(String.valueOf(letter));
  }

  public void setEmpty() {
    this.letter = EMPTY_VALUE;
    setText("");
  }

  public boolean isEmpty() {
    return this.letter == EMPTY_VALUE;
  }
}
