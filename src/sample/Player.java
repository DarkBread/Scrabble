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
  private Button passButton;
  private Label name;
  private String nameOfPlayer;
  private BooleanProperty isMyTurn;

  public Player(String name) {
    this.name = new Label(name);
    nameOfPlayer = name;
    isMyTurn = new SimpleBooleanProperty(false);
    isMyTurn.addListener(observable -> {
      refillFrameWithTiles();
      updateAttributes();
    });
    frame = new Frame();
    attributesOfPlayer = new GridPane();
    doneButton = setUpDoneButton();
    passButton = setUpPassButton();
    setUpLabel();
    attributesOfPlayer.add(this.name, 0, 0);
    attributesOfPlayer.add(frame, 0, 1);
    attributesOfPlayer.add(doneButton, 1, 1);
    attributesOfPlayer.add(passButton, 2, 1);
    GridPane.setMargin(doneButton, new Insets(0, 15, 0, 15));
    updateAttributes();
  }

  public Button getDoneButton() {
    return doneButton;
  }

  public Button getPassButton() {
    return passButton;
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
      passButton.setTextFill(Color.GREEN);
      passButton.setDisable(false);
      name.setTextFill(Color.GREEN);
    } else {
      doneButton.setTextFill(Color.GRAY);
      doneButton.setDisable(true);
      passButton.setTextFill(Color.GRAY);
      passButton.setDisable(true);
      name.setTextFill(Color.GRAY);
    }
  }

  private Button setUpPassButton() {
    Button passButton = new Button("Pass");
    passButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    passButton.setWrapText(true);
    passButton.setTextOverrun(OverrunStyle.CLIP);
    passButton.setOnAction(actionEvent -> {
      if (Board.getInstance().getTilesPlacedOnCurrentTurn().size() != 0) {
        Scrabble.logs.setText("You cannot pass turn if there are your tiles on the board.");
      } else {
        replaceAllReplacingTiles();
        isMyTurn.setValue(false);
      }
    });
    return passButton;
  }

  private void replaceAllReplacingTiles() {
    for (Node node :
            frame.getChildren()) {
      FrameTile tileToReplace = (FrameTile) node;
      if (tileToReplace.isExchangeable()) {
        frame.getChildren().set(frame.getChildren().indexOf(tileToReplace)
                , Pool.getInstance().replaceTile(tileToReplace));
      }
    }
  }


  private Button setUpDoneButton() {
    Button doneButton = new Button("Done");
    doneButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    doneButton.setWrapText(true);
    doneButton.setTextOverrun(OverrunStyle.CLIP);
    doneButton.setOnAction(mouseEvent -> {
      if (Board.getInstance().wordPlacedCorrectly()) {
        updateScore();
        Board.getInstance().makePlacedTilesNotDraggable();
        isMyTurn.setValue(false);
      }
    });
    return doneButton;
  }

  private void updateScore() {
    increaseScoreBy(Board.getInstance().calculateWordScore());
    name.setText(String.format("%s(%d)", nameOfPlayer, score));
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
