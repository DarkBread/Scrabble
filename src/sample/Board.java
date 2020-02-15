package sample;

import javafx.scene.layout.GridPane;

public class Board extends GridPane {

  private final static int BOARD_SIZE = 15;
  private static Board board;
  private Tile[][] tilesOnBoard;
  public static Tile draggedTile;


  private Board() {
    tilesOnBoard = new Tile[BOARD_SIZE][BOARD_SIZE];
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        BoardTile current = new BoardTile();
        current.makeDraggable();
        setDragLogic(current);
        this.add(current, i, j);
      }
    }
    this.setStyle("-fx-border-color: #4276ff;-fx-border-width: 3");
  }

  public void setDragLogic(Tile tile) {
    tile.setOnDragDropped(dragEvent -> {
      int columnIndex = GridPane.getColumnIndex(tile);
      int rowIndex = GridPane.getRowIndex(tile);
      BoardTile replacing = new BoardTile(Board.draggedTile.getLetter());
      replacing.makeDraggable();
      setDragLogic(replacing);
      getInstance().getChildren().remove(tile);
      this.add(replacing, columnIndex, rowIndex);
      dragEvent.setDropCompleted(true);
    });
    tile.setOnDragDone(dragEvent -> {
      int columnIndex = GridPane.getColumnIndex(tile);
      int rowIndex = GridPane.getRowIndex(tile);
      BoardTile replacing = new BoardTile();
      setDragLogic(replacing);
      getInstance().getChildren().remove(tile);
      this.add(replacing, columnIndex, rowIndex);
    });
  }


  public static Board getInstance() {
    if (board == null) {
      board = new Board();
    }
    return board;
  }
}
