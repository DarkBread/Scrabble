package sample;

import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

public class Frame extends HBox {

  private static final int MAX_FRAME_SIZE = 7;

  public Frame() {
    this.setStyle("-fx-border-color: #4276ff;-fx-border-width: 3");
    fillFrameWithTiles();
  }

  private void fillFrameWithTiles() {
    for (int i = 0; i < MAX_FRAME_SIZE; i++) {
      FrameTile drawn = Pool.getInstance().drawRandomFrameTile();
      setDragLogic(drawn);
      getChildren().add(drawn);
    }
  }

  public void refillFrameWithTiles() {
    for (Node frameTile :
            getChildren()) {
      if (((FrameTile) frameTile).isEmpty()) {
        FrameTile replacing = Pool.getInstance().drawRandomFrameTile();
        if (replacing != null) {
          ((FrameTile) frameTile).setLetter(replacing.getLetter());
        }
      }
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

  public void makeAllTilesUnchangeable() {
    for (Node node :
            getChildren()) {
      FrameTile frameTile = (FrameTile) node;
      if (frameTile.isExchangeable()) {
        frameTile.reverseExchangeable();
      }
    }
  }

  public FrameTile getFrameTileWithLetter(char letter) {
    for (Node node : getChildren()) {
      FrameTile frameTile = (FrameTile) node;
      if (frameTile.getLetter() == letter) {
        return frameTile;
      }
    }
    return null;
  }

  /*public boolean thereIsTileWithLetter(char letter) {
    for (Node node : getChildren()) {
      Tile frameTile = (FrameTile) node;
      if (frameTile.getLetter() == letter) {
        return true;
      }
    }
    return false;
  }

  public void removeFirstTileWithLetter(char letter) {
    for (Node node : getChildren()) {
      FrameTile frameTile = (FrameTile) node;
      if (frameTile.getLetter() == letter) {
        frameTile.setEmpty();
        break;
      }
    }
  }*/
}
