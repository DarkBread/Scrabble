package sample;

import javafx.geometry.Insets;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class FrameTile extends Tile {

  private static Background orangeBackground = new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY));
  private boolean exchangeable;

  public FrameTile(char letter) {
    super(letter);
    this.makeDraggable();
  }

  public FrameTile() {
    this.makeDraggable();
  }


  public void makeDraggable() {
    this.setOnDragDetected(mouseEvent -> {
      if (isDraggable() && !isEmpty()) {
        setUpDragAndDropActivity();
        mouseEvent.consume();
      }
    });
    this.setOnDragEntered(dragEvent -> {
      if (Board.draggedTile instanceof BoardTile && this.isEmpty() && isDraggable()) {
        this.setBackground(Tile.getGrayBackground());
        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
      }
    });
    this.setOnDragOver(dragEvent -> {
      if (Board.draggedTile instanceof BoardTile && this.isEmpty() && isDraggable()) {
        dragEvent.acceptTransferModes(TransferMode.ANY);
      }
    });
    this.setOnDragExited(dragEvent -> {
      this.setBackground(Tile.getOrdinalBackground());
    });
    this.setOnMouseClicked(mouseEvent -> {
      reverseExchangeable();
    });
  }

  public void reverseExchangeable() {
    if (isDraggable() && !isEmpty() && !exchangeable) {
      setBackground(orangeBackground);
      exchangeable = true;
    } else {
      setBackground(Tile.getOrdinalBackground());
      exchangeable = false;
    }
  }

  public boolean isExchangeable() {
    return exchangeable;
  }
}



