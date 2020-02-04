package sample;

import java.util.ArrayList;
import java.util.HashMap;

//Singleton patter because there cannot be more than one instance of the class at the same time
public class Pool {

  private static Pool pool;
  private static HashMap<Character, Integer> valuesOfLetters;
  private static HashMap<Character, Integer> amountInStartingPool;
  private ArrayList<Tile> tilesInPool;

  // initializing value of each letter and amount of each letter in the starting pool
  static {
    valuesOfLetters = new HashMap<>();
    valuesOfLetters.put('A', 1);
    valuesOfLetters.put('B', 3);
    valuesOfLetters.put('C', 3);
    valuesOfLetters.put('D', 2);
    valuesOfLetters.put('E', 1);
    valuesOfLetters.put('F', 4);
    valuesOfLetters.put('G', 2);
    valuesOfLetters.put('H', 4);
    valuesOfLetters.put('I', 1);
    valuesOfLetters.put('J', 8);
    valuesOfLetters.put('K', 5);
    valuesOfLetters.put('L', 1);
    valuesOfLetters.put('M', 3);
    valuesOfLetters.put('N', 1);
    valuesOfLetters.put('O', 1);
    valuesOfLetters.put('P', 3);
    valuesOfLetters.put('Q', 10);
    valuesOfLetters.put('R', 1);
    valuesOfLetters.put('S', 1);
    valuesOfLetters.put('T', 1);
    valuesOfLetters.put('U', 1);
    valuesOfLetters.put('V', 4);
    valuesOfLetters.put('W', 4);
    valuesOfLetters.put('X', 8);
    valuesOfLetters.put('Y', 4);
    valuesOfLetters.put('Z', 10);
    valuesOfLetters.put('*', 0);
    // amount of each letter in starting pool
    amountInStartingPool = new HashMap<>();
    amountInStartingPool.put('A', 9);
    amountInStartingPool.put('B', 2);
    amountInStartingPool.put('C', 2);
    amountInStartingPool.put('D', 4);
    amountInStartingPool.put('E', 12);
    amountInStartingPool.put('F', 2);
    amountInStartingPool.put('G', 3);
    amountInStartingPool.put('H', 2);
    amountInStartingPool.put('I', 9);
    amountInStartingPool.put('J', 1);
    amountInStartingPool.put('K', 1);
    amountInStartingPool.put('L', 4);
    amountInStartingPool.put('M', 2);
    amountInStartingPool.put('N', 2);
    amountInStartingPool.put('O', 8);
    amountInStartingPool.put('P', 2);
    amountInStartingPool.put('Q', 1);
    amountInStartingPool.put('R', 6);
    amountInStartingPool.put('S', 4);
    amountInStartingPool.put('T', 6);
    amountInStartingPool.put('U', 4);
    amountInStartingPool.put('V', 2);
    amountInStartingPool.put('W', 2);
    amountInStartingPool.put('X', 1);
    amountInStartingPool.put('Y', 2);
    amountInStartingPool.put('Z', 1);
    amountInStartingPool.put('*', 2);
  }

  private Pool() {
    tilesInPool = new ArrayList<>(100);
    fillPoolWithTiles();
  }

  public static Pool getInstance() {
    if (pool == null) {
      pool = new Pool();
    }
    return pool;
  }

  private void fillPoolWithTiles() {
    for (Character letter :
            valuesOfLetters.keySet()) {
      for (int i = 0; i < amountInStartingPool.get(letter); i++) {
        tilesInPool.add(new Tile(letter));
      }
    }
  }

  public void reset() {
    tilesInPool.clear();
    fillPoolWithTiles();
  }

  public boolean isEmpty() {
    return tilesInPool.isEmpty();
  }

  public Tile drawRandomTile() {
    int indexOfRandomTile = (int) (Math.random() * tilesInPool.size());
    Tile drawn = tilesInPool.get(indexOfRandomTile);
    tilesInPool.remove(indexOfRandomTile);
    return drawn;
  }

  public static int getValueOfLetter(char letter) {
    return valuesOfLetters.get(letter);
  }

  public int numberOfTilesInThePool() {
    return tilesInPool.size();
  }
}
