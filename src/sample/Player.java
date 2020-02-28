package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Player {

  private int score;
  private Frame frame;
  private GridPane attributesOfPlayer;
  private Button doneButton;
  private Button skipButton;
  private Label name;
  private BooleanProperty isMyTurn;

  public Player(String name) {
    this.name = new Label(name);
    isMyTurn = new SimpleBooleanProperty(false);
    isMyTurn.addListener(observable -> {
      refillFrameWithTiles();
      updateAttributes();
    });
    frame = new Frame();
    attributesOfPlayer = new GridPane();
    doneButton = setUpDoneButton();
    skipButton = setUpSkipButton();
    setUpLabel();
    attributesOfPlayer.add(this.name, 0, 0);
    attributesOfPlayer.add(frame, 0, 1);
    attributesOfPlayer.add(doneButton, 1, 1);
    attributesOfPlayer.add(skipButton, 2, 1);
    GridPane.setMargin(doneButton, new Insets(0, 15, 0, 15));
    updateAttributes();
  }

  private void updateAttributes() {
    updateButtons();
    updateName();
    updateFrame();
  }

  private void updateFrame() {
    for (Node node :
            frame.getChildren()) {
      if (isMyTurn()) {
        ((Tile) node).setDraggable(true);
      } else {
        ((Tile) node).setDraggable(false);
      }
    }
  }

  private void updateName() {
    if (isMyTurn.getValue()) {
      name.setTextFill(Color.GREEN);
    } else {
      name.setTextFill(Color.GRAY);
    }
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
      name.setTextFill(Color.GREEN);
    } else {
      doneButton.setTextFill(Color.GRAY);
      doneButton.setDisable(true);
      skipButton.setTextFill(Color.GRAY);
      skipButton.setDisable(true);
      name.setTextFill(Color.GRAY);
    }
  }
  private Button setUpSkipButton() {
    Button skipButton = new Button("Skip");
    skipButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    skipButton.setWrapText(true);
    skipButton.setTextOverrun(OverrunStyle.CLIP);
    skipButton.setOnMouseClicked(mouseEvent -> {
    });
    return skipButton;
  }


  private Button setUpDoneButton() {
    Button doneButton = new Button("Done");
    doneButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    doneButton.setWrapText(true);
    doneButton.setTextOverrun(OverrunStyle.CLIP);
    doneButton.setOnMouseClicked(mouseEvent -> {
      if (Board.getInstance().wordPlacedCorrectly()) {
        Board.getInstance().makePlacedTilesNotDraggable();
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
    name.setTextFill(Color.BLACK);
    name.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    return name;
  }


  public String getName() {
    return name.getText();
  }

  public void setName(String name) {
    this.name.setText(name);
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
