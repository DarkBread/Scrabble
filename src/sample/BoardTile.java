package sample;

public class BoardTile extends Tile {

  public BoardTile() {
    this.makeDraggable();
  }

  public BoardTile(char letter) {
    super(letter);
    this.makeDraggable();
  }
}
