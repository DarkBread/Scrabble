package sample;

import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Comparator;

class Board extends GridPane {

  private final static int BOARD_SIZE = 15;
  public static Tile draggedTile;
  private static Board board;
  private static ArrayList<Tile> tilesPlacedOnCurrentTurn = new ArrayList<>();
  private boolean firstWord = true;

  private Board() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        BoardTile current = new BoardTile();
        this.add(current, i, j);

      }
    }
    drawGameBoard();
    this.setStyle("-fx-border-color: #4276ff;-fx-border-width: 3");
  }

  public static void setDragLogic(Tile tile) {
    tile.setOnDragDropped(dragEvent -> {
      if (tile.isEmpty()) {
        tile.setLetter(draggedTile.getLetter());
        tilesPlacedOnCurrentTurn.add(tile);
        dragEvent.setDropCompleted(true);
      } else {
        dragEvent.setDropCompleted(false);
      }
    });
    tile.setOnDragDone(dragEvent -> {
      if (dragEvent.getTransferMode() == TransferMode.MOVE) {
        tilesPlacedOnCurrentTurn.remove(tile);
        tile.setEmpty();
      }
    });
  }

  public static Board getInstance() {
    if (board == null) {
      board = new Board();
    }
    return board;
  }

  private void drawGameBoard() {
    //Initialization of the board
    //start
    getChildren().remove(getTileByRowColumnIndex(BOARD_SIZE / 2, BOARD_SIZE / 2));
    add(new BonusTile(BonusTile.Type.START), BOARD_SIZE / 2, BOARD_SIZE / 2);
    //Triple world score
    for (int i = 0; i < 15; i = i + 7) {
      add(new BonusTile(BonusTile.Type.TRIPLE_WORD_SCORE), 0, i);
      add(new BonusTile(BonusTile.Type.TRIPLE_WORD_SCORE), 14, i);
    }
    add(new BonusTile(BonusTile.Type.TRIPLE_WORD_SCORE), 7, 0);
    add(new BonusTile(BonusTile.Type.TRIPLE_WORD_SCORE), 7, 14);
    //Double word score
    for (int i = 1; i < 14; i++) {
      if (i != 5 && i != 6 && i != 7 && i != 8 && i != 9) {
        add(new BonusTile(BonusTile.Type.DOUBLE_WORD_SCORE), i, i);
        add(new BonusTile(BonusTile.Type.DOUBLE_WORD_SCORE), i, 14 - i);
      }
    }
    //Triple letter score
    for (int i = 1; i < 14; i = i + 4) {
      add(new BonusTile(BonusTile.Type.TRIPLE_LETTER_SCORE), 5, i);
      add(new BonusTile(BonusTile.Type.TRIPLE_LETTER_SCORE), 9, i);
      if (i != 1 && i != 13) {
        add(new BonusTile(BonusTile.Type.TRIPLE_WORD_SCORE), 1, i);
        add(new BonusTile(BonusTile.Type.TRIPLE_WORD_SCORE), 13, i);
      }
    }
    //Double letter score
    for (int i = 3; i < 15; i = i + 8) {
      add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 0, i);
      add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 7, i);
      add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 14, i);
    }
    for (int i = 0; i < 15; i = i + 7) {
      add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 4, i);
      add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 10, i);
    }
    for (int i = 2; i <= 12; i = i + 2) {
      if (i != 4 && i != 10) {
        add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 6, i);
        add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 8, i);
      }
    }
    for (int i = 6; i <= 8; i = i + 2) {
      add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 2, i);
      add(new BonusTile(BonusTile.Type.DOUBLE_LETTER_SCORE), 12, i);
    }
  }

  public void makePlacedTilesNotDraggable() {
    for (Tile placedTile :
            tilesPlacedOnCurrentTurn) {
      placedTile.setStyle("-fx-border-color: #737f80;-fx-font-weight: bold;-fx-font-style: italic;-fx-border-width: 3");
      placedTile.setOnDragDone(null);
      placedTile.setOnDragDetected(null);
      placedTile.setOnDragOver(null);
      placedTile.setOnDragEntered(null);
      placedTile.setOnDragExited(null);
      placedTile.setDraggable(false);
    }
    tilesPlacedOnCurrentTurn.clear();
  }

  public boolean wordPlacedCorrectly() {
    if (getStartTile().isEmpty()) {
      Scrabble.logs.setText("First Word Should Be Placed At The Start Tile");
      return false;
    }
    if (wordPlacedHorizontally()) {
      tilesPlacedOnCurrentTurn.sort(Comparator.comparingInt(GridPane::getColumnIndex));
      for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
        if (GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i)) + 1 !=
                (GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i + 1)))) {
          int row = GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i));
          int column = GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i)) + 1;
          Tile candidateForConnectingTile = getTileByRowColumnIndex(row, column);
          if (!candidateForConnectingTile.isDraggable()) {
            int rowOfConnectingTile = GridPane.getRowIndex(candidateForConnectingTile);
            int columnOfConnectingTile = GridPane.getColumnIndex(candidateForConnectingTile);
            tilesPlacedOnCurrentTurn.add(i + 1, getTileByRowColumnIndex(rowOfConnectingTile, columnOfConnectingTile));
          } else {
            return false;
          }
        }
      }
      if (!thereIsConnectingTile()) {
        for (Tile tile :
                tilesPlacedOnCurrentTurn) {
          System.out.println(tile.getLetter());
        }
        Tile connecting = getTileByRowColumnIndex(GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(0)),
                GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(0)) - 1);
        System.out.println(connecting.getLetter());
        if (!connecting.isDraggable()) {
          tilesPlacedOnCurrentTurn.add(0, connecting);
        }
      }
    } else if (wordPlacedVertically()) {
      tilesPlacedOnCurrentTurn.sort(Comparator.comparingInt(GridPane::getRowIndex));
      for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
        if (GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i)) + 1 !=
                (GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i + 1)))) {
          int row = GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i)) + 1;
          int column = GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i));
          Tile candidateForConnectingTile = getTileByRowColumnIndex(row, column);
          if (!candidateForConnectingTile.isDraggable()) {
            int rowOfConnectingTile = GridPane.getRowIndex(candidateForConnectingTile);
            int columnOfConnectingTile = GridPane.getColumnIndex(candidateForConnectingTile);
            tilesPlacedOnCurrentTurn.add(i + 1, getTileByRowColumnIndex(rowOfConnectingTile, columnOfConnectingTile));
          } else {
            return false;
          }
        }
      }
      if (!thereIsConnectingTile()) {
        Tile connecting = getTileByRowColumnIndex(GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(0)) - 1,
                GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(0)));
        if (!connecting.isDraggable()) {
          tilesPlacedOnCurrentTurn.add(0, connecting);
        }
      }
    } else {
      return false;
    }
    if (!thereIsConnectingTile() && !firstWord) {
      return false;
    }
    firstWord = false;
    Scrabble.logs.setText("");
    return true;
  }

  private boolean thereIsConnectingTile() {
    for (Tile tile :
            tilesPlacedOnCurrentTurn) {
      System.out.println(tile.getLetter() + ":" + tile.isDraggable());
    }
    for (Tile tile :
            tilesPlacedOnCurrentTurn) {
      System.out.println(tile.getLetter());
      if (!tile.isDraggable()) {
        return true;
      }
    }
    return false;
  }

  private boolean wordPlacedVertically() {
    for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
      if (!(GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i)).equals
              (GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i + 1))))) {
        return false;
      }
    }
    return true;
  }

  private boolean wordPlacedHorizontally() {
    for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
      if (!(GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i)).equals
              (GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i + 1))))) {
        return false;
      }
    }
    return true;
  }

  private Tile getStartTile() {
    return getTileByRowColumnIndex(BOARD_SIZE / 2, BOARD_SIZE / 2);
  }


  public Tile getTileByRowColumnIndex(int row, int column) {
    Tile result = null;
    for (Node boardTile : getChildren()) {
      if (GridPane.getRowIndex(boardTile) == row && GridPane.getColumnIndex(boardTile) == column) {
        result = (Tile) boardTile;
        break;
      }
    }
    return result;
  }
}

