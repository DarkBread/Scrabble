package sample;

import javafx.scene.input.TransferMode;

public class FrameTile extends Tile {


  public FrameTile(char letter) {
    super(letter);
    this.makeDraggable();
  }

  public FrameTile() {
    this.makeDraggable();
  }


  public void makeDraggable() {
    this.setOnDragDetected(mouseEvent -> {
      if (isDraggable()) {
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
  }
}



