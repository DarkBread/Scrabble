package sample;

import javafx.scene.input.TransferMode;

public class BoardTile extends Tile {

  public BoardTile() {
    Board.setDragLogic(this);
    this.makeDraggable();
  }

  public BoardTile(char letter) {
    super(letter);
    Board.setDragLogic(this);
    this.makeDraggable();
  }

  public void makeDraggable() {
    this.setOnDragDetected(mouseEvent -> {
      if (!this.isEmpty()) {
        setUpDragAndDropActivity();
        mouseEvent.consume();
      }
    });
    this.setOnDragEntered(dragEvent -> {
      this.setBackground(Tile.getGrayBackground());
      dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
    });
    this.setOnDragOver(dragEvent -> {
      dragEvent.acceptTransferModes(TransferMode.ANY);
    });
    this.setOnDragExited(dragEvent -> {
      this.setBackground(Tile.getOrdinalBackground());
    });
  }
}
