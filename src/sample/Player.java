package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Player {

  private String name;
  private int score;
  private Frame frame;
  private GridPane attributesOfPlayer;
  private Button doneButton;
  private Button skipButton;
  private Label nameOfPlayer;
  private BooleanProperty isMyTurn;

  public Player(String name) {
    this.name = name;
    isMyTurn = new SimpleBooleanProperty(false);
    isMyTurn.addListener(observable -> {
      updateButtons();
      refillFrameWithTiles();
    });
    frame = new Frame();
    attributesOfPlayer = new GridPane();
    doneButton = setUpDoneButton();
    skipButton = setUpSkipButton();
    nameOfPlayer = setUpLabel();
    attributesOfPlayer.add(nameOfPlayer, 0, 0);
    attributesOfPlayer.add(frame, 0, 1);
    attributesOfPlayer.add(doneButton, 1, 1);
    attributesOfPlayer.add(skipButton, 2, 1);
    GridPane.setMargin(doneButton, new Insets(0, 15, 0, 15));
    updateButtons();
  }

  public void refillFrameWithTiles() {
    frame.refillFrameWithTiles();
  }

  public GridPane getAttributes() {
    return attributesOfPlayer;
  }

  private void updateButtons() {
    if (isMyTurn.getValue()) {
      doneButton.setTextFill(Color.GREEN);
      doneButton.setDisable(false);
      skipButton.setTextFill(Color.GREEN);
      skipButton.setDisable(false);
      nameOfPlayer.setTextFill(Color.GREEN);
    } else {
      doneButton.setTextFill(Color.GRAY);
      doneButton.setDisable(true);
      skipButton.setTextFill(Color.GRAY);
      skipButton.setDisable(true);
      nameOfPlayer.setTextFill(Color.GRAY);
    }
  }

  private Button setUpSkipButton() {
    Button skipButton = new Button("Skip");
    skipButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    skipButton.setOnMouseClicked(mouseEvent -> {
    });
    return skipButton;
  }


  private Button setUpDoneButton() {
    Button doneButton = new Button("Done");
    doneButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    doneButton.setOnMouseClicked(mouseEvent -> {
      if (Board.getInstance().wordPlacedCorrectly()) {
        isMyTurn.setValue(false);
      }
    });
    return doneButton;
  }

  public boolean isMyTurn() {
    return isMyTurn.getValue();
  }

  public void setIsMyTurn(boolean isMyTurn) {
    this.isMyTurn.set(isMyTurn);
  }

  private Label setUpLabel() {
    Label nameOfPlayer = new Label(name);
    nameOfPlayer.setTextFill(Color.BLACK);
    nameOfPlayer.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    return nameOfPlayer;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void increaseScoreBy(int points) {
    score += points;
  }

  public Frame getFrame() {
    return frame;
  }
}
