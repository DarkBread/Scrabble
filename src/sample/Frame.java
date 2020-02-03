package sample;

public class Frame {

  private static final int MAX_FRAME_SIZE = 7;
  private Tile[] tilesInFrame;

  public Frame() {
    this.tilesInFrame = new Tile[MAX_FRAME_SIZE];
  }

  public void fillFrameWithTiles() {
    for (int i = 0; i < tilesInFrame.length; i++) {
      if (tilesInFrame[i].isEmpty()) {
        tilesInFrame[i] = Pool.getInstance().drawRandomTile();
      }
    }
  }

  public boolean thereIsTileWithLetter(char letter) {
    for (Tile tileInFrame : tilesInFrame) {
      if (tileInFrame.getLetter() == letter) {
        return true;
      }
    }
    return false;
  }

  public void removeFirstTileWithLetter(char tile) {
    for (int i = 0; i < tilesInFrame.length; i++) {
      if (tilesInFrame[i].getLetter() == tile) {
        tilesInFrame[i].setEmpty();
        break;
      }
    }
  }
}
