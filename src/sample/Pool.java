package sample;

import java.util.ArrayList;
import java.util.HashMap;

//Singleton patter because there cannot be more than one instance of the class at the same time
public class Pool {

  private static Pool pool;
  private static HashMap<Character, Integer> valuesOfLetters;

  // initialize value of each letter,needs to be implemented later
  static {
  }

  private ArrayList<Tile> tilesInPool;

  private Pool() {
    tilesInPool = new ArrayList<>(100);
    fillPoolWithLetters();
  }

  public static Pool getInstance() {
    if (pool == null) {
      pool = new Pool();
    }
    return pool;
  }

  //Will write later on
  private void fillPoolWithLetters() {
  }

  public void reset() {
    fillPoolWithLetters();
  }

  public boolean isEmpty() {
    return tilesInPool.isEmpty();
  }

  public Tile drawRandomTile() {
    return tilesInPool.get(indexOfRandomTileInRangeOfPoolSize());
  }

  private int indexOfRandomTileInRangeOfPoolSize() {
    return (int) (Math.random() * tilesInPool.size());
  }

  public static int getValueOfLetter(char letter) {
    return valuesOfLetters.get(letter);
  }

  public int numberOfTilesInThePool() {
    return tilesInPool.size();
  }
}
