package sample;

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
}
