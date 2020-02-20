package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


class Tile extends Label {

  static final int EMPTY_VALUE = 0;
  private static Background ordinalBackground = new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY));
  private static Background grayBackground = new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY));
  private char letter;
  private boolean draggable;

  Tile(char letter) {
    this();
    setLetter(letter);
  }

  Tile() {
    draggable = true;
    setStyle("-fx-border-color: #ffb144;-fx-font-weight: bold");
    setPrefSize(40, 40);
    setAlignment(Pos.CENTER);
  }

  static Background getGrayBackground() {
    return grayBackground;
  }

  static Background getOrdinalBackground() {
    return ordinalBackground;
  }

  boolean isDraggable() {
    return draggable;
  }

  void setDraggable(boolean draggable) {
    this.draggable = draggable;
  }

  char getLetter() {
    return letter;
  }

  void setLetter(char letter) {
    this.letter = letter;
    setText(String.valueOf(letter));
  }

  void setEmpty() {
    this.letter = EMPTY_VALUE;
    setText("");
  }

  void setUpDragAndDropActivity() {
    this.setBackground(Tile.getGrayBackground());
    Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);
    ClipboardContent content = new ClipboardContent();
    content.putString(this.getText());
    dragboard.setContent(content);
    Board.draggedTile = this;
  }

  boolean isEmpty() {
    return this.letter == EMPTY_VALUE;
  }
}
