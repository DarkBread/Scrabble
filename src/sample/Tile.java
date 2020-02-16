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


public class Tile extends Label {

  public static final int EMPTY_VALUE = 0;
  private char letter;
  protected Background background = new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY));


  public Tile(char letter) {
    this();
    setLetter(letter);
  }

  public Tile() {
    setStyle("-fx-border-color: #ffb144;-fx-font-weight: bold");
    setPrefSize(40, 40);
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

  public void makeDraggable() {
    this.setOnDragDetected(mouseEvent -> {
      if (!this.isEmpty()) {
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(this.getText());
        dragboard.setContent(content);
        Board.draggedTile = this;
        mouseEvent.consume();
      }
    });
    this.setOnDragEntered(dragEvent -> {
      this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
      dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
    });
    this.setOnDragOver(dragEvent -> {
      dragEvent.acceptTransferModes(TransferMode.ANY);
    });
    this.setOnDragExited(dragEvent -> {
      this.setBackground(background);
    });
  }

  public boolean isEmpty() {
    return this.letter == EMPTY_VALUE;
  }
}
