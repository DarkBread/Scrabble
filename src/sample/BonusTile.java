package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BonusTile extends BoardTile {

  private Type type;
  private Background background;
  public BonusTile(Type type) {
    this.type = type;
    setColor();
    setTooltip(new Tooltip(type.getTooltip()));
    setOnDragExited(dragEvent -> setBackground(background));
  }

  public Type getType() {
    return type;
  }

  private void setColor() {
    switch (type) {
      case START:
        background = new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case DOUBLE_LETTER_SCORE:
        background = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case TRIPLE_LETTER_SCORE:
        background = new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case DOUBLE_WORD_SCORE:
        background = new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY));
        break;
      case TRIPLE_WORD_SCORE:
        background = new Background(new BackgroundFill(Color.DEEPPINK, CornerRadii.EMPTY, Insets.EMPTY));
    }
    setBackground(background);
  }

  enum Type {
    DOUBLE_LETTER_SCORE("Double Letter Score", 2, false),
    TRIPLE_LETTER_SCORE("Triple Letter Score", 3, false),
    DOUBLE_WORD_SCORE("Double Word Score", 2, true),
    TRIPLE_WORD_SCORE("Triple Word Score", 3, true),
    START("First Word Should Start From Here", 1, false);
    private String tooltip;
    private int multiplier;
    private boolean wholeWordMultiplier;

    Type(String tooltip, int multiplier, boolean wholeWordMultiplier) {
      this.tooltip = tooltip;
      this.multiplier = multiplier;
      this.wholeWordMultiplier = wholeWordMultiplier;
    }


    public int getMultiplier() {
      return multiplier;
    }

    public boolean isWholeWordMultiplier() {
      return wholeWordMultiplier;
    }

    public String getTooltip() {
      return tooltip;
    }
  }
}
