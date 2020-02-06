package sample;

import javafx.scene.layout.HBox;

public class Frame extends HBox {

  private static final int MAX_FRAME_SIZE = 7;
  private Tile[] tilesInFrame;

  public Frame() {
    this.tilesInFrame = new Tile[MAX_FRAME_SIZE];
  }

  public void fillFrameWithTiles() {
    for (int i = 0; i < MAX_FRAME_SIZE; i++) {
      if (tilesInFrame[i] == null || tilesInFrame[i].isEmpty()) {
        tilesInFrame[i] = Pool.getInstance().drawRandomTile();
      }
    }
    getChildren().addAll(tilesInFrame);
  }

  public boolean thereIsTileWithLetter(char letter) {
    for (Tile tileInFrame : tilesInFrame) {
      if (tileInFrame.getLetter() == letter) {
        return true;
      }
    }
    return false;
  }

  public void removeFirstTileWithLetter(char letter) {
    for (Tile tileInFrame : tilesInFrame) {
      if (tileInFrame.getLetter() == letter) {
        tileInFrame.setEmpty();
        break;
      }
    }
  }
}
