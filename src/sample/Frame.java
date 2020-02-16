package sample;

import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

public class Frame extends HBox {

  private static final int MAX_FRAME_SIZE = 7;

  public Frame() {
  }

  public void fillFrameWithTiles() {
    for (int i = 0; i < MAX_FRAME_SIZE; i++) {
      Tile drawn = Pool.getInstance().drawRandomFrameTile();
      setDragLogic(drawn);
      getChildren().add(drawn);
    }
  }

  private void setDragLogic(Tile tile) {
    tile.setOnDragDone(dragEvent -> {
      if (dragEvent.getTransferMode() == TransferMode.MOVE) {
        tile.setLetter((char) Tile.EMPTY_VALUE);
      }
    });
    tile.setOnDragDropped(dragEvent -> {
      if (Board.draggedTile instanceof BoardTile && tile.isEmpty()) {
        tile.setLetter(Board.draggedTile.getLetter());
        dragEvent.setDropCompleted(true);
      } else {
        dragEvent.setDropCompleted(false);
      }
    });
  }
}






  /*public boolean thereIsTileWithLetter(char letter) {
    for (Tile tileInFrame : getChildren()) {
      if (tileInFrame.getLetter() == letter) {
        return true;
      }
    }
    return false;
  }
*/
  /*public void removeFirstTileWithLetter(char letter) {
    for (Tile tileInFrame : getChildren()) {
      if (tileInFrame.getLetter() == letter) {
        tileInFrame.setEmpty();
        break;
      }
    }
  }
}
*/