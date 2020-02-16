package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BonusTile extends BoardTile {

  Type type;

  public BonusTile(Type type) {
    this.type = type;
    setColor();
    setOnDragExited(dragEvent -> setBackground(background));
  }

  private void setColor() {
    switch (type) {
      case START:
        background = new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case DOUBLE_LETTER_SCORE:
        background = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case TRIPLE_LETTER_SCORE:
        background = new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case DOUBLE_WORD_SCORE:
        background = new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case TRIPLE_WORD_SCORE:
        background = new Background(new BackgroundFill(Color.DEEPPINK, CornerRadii.EMPTY, Insets.EMPTY));
    }
    setBackground(background);
  }

  enum Type {DOUBLE_LETTER_SCORE, TRIPLE_LETTER_SCORE, DOUBLE_WORD_SCORE, TRIPLE_WORD_SCORE, START}
}
