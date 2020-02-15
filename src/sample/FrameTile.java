package sample;

import javafx.geometry.Insets;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class FrameTile extends Tile {

  public FrameTile(char letter) {
    super(letter);
    this.makeDraggable();
  }

  public FrameTile() {
    this.makeDraggable();
  }

  @Override
  public void makeDraggable() {
    this.setOnDragDetected(mouseEvent -> {
      this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
      Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);
      ClipboardContent content = new ClipboardContent();
      content.putString(this.getText());
      dragboard.setContent(content);
      Board.draggedTile = this;
      mouseEvent.consume();
    });
    this.setOnDragEntered(dragEvent -> {
      if (Board.draggedTile instanceof BoardTile && this.isEmpty()) {
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
      }
    });
    this.setOnDragOver(dragEvent -> {
      if (Board.draggedTile instanceof BoardTile && this.isEmpty()) {
        dragEvent.acceptTransferModes(TransferMode.ANY);
      }
    });
    this.setOnDragExited(dragEvent -> {
      this.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
    });
  }
}



