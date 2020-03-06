package sample;

import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

class Board extends GridPane {

  private final static int BOARD_SIZE = 15;
  public static Tile draggedTile;
  private static Board board;
  private ArrayList<Tile> tilesPlacedOnCurrentTurn = new ArrayList<>();
  private boolean firstWord = true;

  private Board() {
    drawGameBoard();
    this.setStyle("-fx-border-color: #4276ff;-fx-border-width: 3");
  }

  public static void setDragLogic(Tile tile) {
    tile.setOnDragDropped(dragEvent -> {
      dragEvent.setDropCompleted(replaceTileOnBoardIfEmpty(tile));
    });
    tile.setOnDragDone(dragEvent -> {
      if (dragEvent.getTransferMode() == TransferMode.MOVE) {
        getInstance().tilesPlacedOnCurrentTurn.remove(tile);
        tile.setEmpty();
      }
    });
  }

  public static boolean replaceTileOnBoardIfEmpty(Tile tile) {
    if (tile.isEmpty()) {
      tile.setLetter(draggedTile.getLetter());
      getInstance().tilesPlacedOnCurrentTurn.add(tile);
      return true;
    } else {
      return false;
    }
  }

  public static Board getInstance() {
    if (board == null) {
      board = new Board();
    }
    return board;
  }

  static void placeWordVertically(String letters, int startingRow, int startingColumn) {
    for (int i = 0; i < letters.length(); i++) {
      //made checks
      FrameTile frameTile = Scrabble.getCurrentPlayer().getFrame().getFrameTileWithLetter(letters.charAt(i));
      if (frameTile != null) {
        Board.draggedTile = frameTile;
        Board.replaceTileOnBoardIfEmpty(Board.getInstance().getTileByRowColumnIndex(startingRow + i, startingColumn));
        Scrabble.getCurrentPlayer().getFrame().removeFirstTileWithLetter(frameTile.getLetter());
      }
    }
    Scrabble.getCurrentPlayer().getDoneButton().fire();
  }

  static void placeWordHorizontally(String letters, int startingRow, int startingColumn) {
    for (int i = 0; i < letters.length(); i++) {
      //make checks
      FrameTile frameTile = Scrabble.getCurrentPlayer().getFrame().getFrameTileWithLetter(letters.charAt(i));
      if (frameTile != null) {
        Board.draggedTile = frameTile;
        Board.replaceTileOnBoardIfEmpty(Board.getInstance().getTileByRowColumnIndex(startingRow, startingColumn + i));
        Scrabble.getCurrentPlayer().getFrame().removeFirstTileWithLetter(frameTile.getLetter());
      }
    }
    Scrabble.getCurrentPlayer().getDoneButton().fire();
  }

  public static String getWord() {
    StringBuilder sb = new StringBuilder();
    for (Tile tile :
            getInstance().tilesPlacedOnCurrentTurn) {
      sb.append(tile.getLetter());
    }
    return sb.toString();
  }

  public boolean thereAreDraggableTilesOnBoard() {
    for (Tile tile :
            tilesPlacedOnCurrentTurn) {
      if (tile.isDraggable()) {
        return true;
      }
    }
    return false;
  }

  public boolean checkIfThereIsWord(String word) {
    try (BufferedReader fileReader = new BufferedReader(new FileReader("src/Resources/words.txt"))) {
      String readWord;
      while ((readWord = fileReader.readLine()) != null) {
        if (readWord.trim().equalsIgnoreCase(word)) {
          Scrabble.logs.setText(Scrabble.logs.getText() + "\nThere is such a word");
          return true;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Scrabble.logs.setText(Scrabble.logs.getText() + "\nThere is NO such a word");
    return false;
  }

  public ArrayList<Tile> getTilesPlacedOnCurrentTurn() {
    return tilesPlacedOnCurrentTurn;
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
    //add missing tiles
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        if (getTileByRowColumnIndex(j, i) == null) {
          this.add(new BoardTile(), i, j);
        }
      }
    }
    printGameBoardToConsole();
  }

  void makePlacedTilesNotDraggable() {
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
    printGameBoardToConsole();
    tilesPlacedOnCurrentTurn.clear();
  }

  public void setFirstWord(boolean firstWord) {
    this.firstWord = firstWord;
  }

  boolean wordPlacedCorrectly() {
    if (tilesPlacedOnCurrentTurn.isEmpty()) {
      return false;
    }
    if (getStartTile().isEmpty()) {
      Scrabble.logs.setText("First Word Should Be Placed At The Start Tile");
      return false;
    }
    boolean connectingTileWasFound = false;
    if (wordPlacedHorizontally()) {
      tilesPlacedOnCurrentTurn.sort(Comparator.comparingInt(GridPane::getColumnIndex));
      for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
        if (twoTilesAreNotNextToEachOtherAtTheSameRow(i)) {
          Tile connectingTile = getPossibleConnectingTileToRight(i);
          if (connectingTile != null) {
            tilesPlacedOnCurrentTurn.add(i + 1, connectingTile);
            connectingTileWasFound = true;
          } else {
            return false;
          }
        }
      }
      if (!connectingTileWasFound) {
        Tile connectingTile = getPossibleConnectingTileToLeft();
        if (connectingTile != null) {
          tilesPlacedOnCurrentTurn.add(0, connectingTile);
          connectingTileWasFound = true;
        }
      }
    } else if (wordPlacedVertically()) {
      tilesPlacedOnCurrentTurn.sort(Comparator.comparingInt(GridPane::getRowIndex));
      for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
        if (twoTilesAreNotNextToEachOtherAtTheSameColumn(i)) {
          Tile connectingTile = getPossibleConnectingTileFromBelow(i);
          if (connectingTile != null) {
            tilesPlacedOnCurrentTurn.add(i + 1, connectingTile);
            connectingTileWasFound = true;
          } else {
            return false;
          }
        }
      }
      if (!connectingTileWasFound) {
        Tile connecting = getPossibleConnectingTileFromAbove();
        if (connecting != null) {
          tilesPlacedOnCurrentTurn.add(0, connecting);
          connectingTileWasFound = true;
        }
      }
    } else {
      return false;
    }
    if (!connectingTileWasFound && getStartTile().isEmpty()) {
      return false;
    }
    Scrabble.logs.setText("");
    firstWord = false;
    return true;
  }

  private Tile getPossibleConnectingTileFromAbove() {
    int rowOfConnectingTile = GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(0)) - 1;
    int columnOfConnectingTile = GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(0));
    if (rowOfConnectingTile < 0) {
      return null;
    }
    Tile connecting = getTileByRowColumnIndex(rowOfConnectingTile, columnOfConnectingTile);
    if (connecting.isEmpty()) {
      return null;
    }
    return connecting;
  }

  private Tile getPossibleConnectingTileFromBelow(int indexOfConnectingTileFromAbove) {
    int row = GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(indexOfConnectingTileFromAbove)) + 1;
    int column = GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(indexOfConnectingTileFromAbove));
    Tile connecting = getTileByRowColumnIndex(row, column);
    if (connecting.isEmpty()) {
      return null;
    }
    return connecting;
  }

  private boolean twoTilesAreNotNextToEachOtherAtTheSameColumn(int i) {
    return GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i)) + 1 !=
            (GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i + 1)));
  }

  private Tile getPossibleConnectingTileToLeft() {
    int rowOfConnectingTile = GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(0));
    int columnOfConnectingTile = GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(0)) - 1;
    if (columnOfConnectingTile < 0) {
      return null;
    }
    Tile connecting = getTileByRowColumnIndex(rowOfConnectingTile, columnOfConnectingTile);
    if (connecting.isEmpty()) {
      return null;
    }
    return connecting;
  }

  private Tile getPossibleConnectingTileToRight(int indexOfTileToLeftFromConnectingTile) {
    int row = GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(indexOfTileToLeftFromConnectingTile));
    int column = GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(indexOfTileToLeftFromConnectingTile)) + 1;
    Tile candidateForConnectingTile = getTileByRowColumnIndex(row, column);
    if (candidateForConnectingTile.isEmpty()) {
      return null;
    } else {
      return candidateForConnectingTile;
    }
  }

  private boolean twoTilesAreNotNextToEachOtherAtTheSameRow(int indexOfFirstTile) {
    return GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(indexOfFirstTile)) + 1 !=
            (GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(indexOfFirstTile + 1)));
  }

  private boolean wordPlacedVertically() {
    if (tilesPlacedOnCurrentTurn.size() == 1 && getPossibleConnectingTileFromAbove() == null) {
      return false;
    }
    for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
      if (!(GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i)).equals
              (GridPane.getColumnIndex(tilesPlacedOnCurrentTurn.get(i + 1))))) {
        return false;
      }
    }
    return true;
  }

  private boolean wordPlacedHorizontally() {
    if (tilesPlacedOnCurrentTurn.size() == 1 && getPossibleConnectingTileToLeft() == null) {
      return false;
    }
    for (int i = 0; i < tilesPlacedOnCurrentTurn.size() - 1; i++) {
      if (!(GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i)).equals
              (GridPane.getRowIndex(tilesPlacedOnCurrentTurn.get(i + 1))))) {
        return false;
      }
    }
    return true;
  }

  private void printGameBoardToConsole() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        System.out.print(getTileByRowColumnIndex(i, j) + "|");
      }
      System.out.println();
    }
    System.out.println("===============================");
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

  public int calculateWordScore() {
    int scoreForWord = 0;
    int multiplier = 1;
    for (Tile tile : tilesPlacedOnCurrentTurn) {
      if (tile instanceof BonusTile) {
        BonusTile bonusTile = (BonusTile) tile;
        if (bonusTile.getType().isWholeWordMultiplier()) {
          multiplier *= bonusTile.getType().getMultiplier();
        } else {
          scoreForWord += Pool.getValueOfLetter(bonusTile.getLetter()) * bonusTile.getType().getMultiplier();
        }
      } else {
        scoreForWord += Pool.getValueOfLetter(tile.getLetter());
      }
    }
    return scoreForWord * multiplier;
  }
}

