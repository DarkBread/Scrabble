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
  private Button challengeButton;
  private Label name;
  private BooleanProperty isMyTurn;
  private BooleanProperty challengeMode;
  private boolean skipNextTurn;
  private boolean needsToRemoveAllTilesFromBoardAndSkip;

  public Player(String name) {
    this.name = new Label(name);
    isMyTurn = new SimpleBooleanProperty(false);
    isMyTurn.addListener(observable -> {
      updateAttributes();
      if (!isMyTurn.get() && !needsToRemoveAllTilesFromBoardAndSkip) {
        updateScore();
        Board.getInstance().makePlacedTilesNotDraggable();
        refillFrameWithTiles();
      } else if (isMyTurn.get() && skipNextTurn) {
        skipNextTurn = false;
        Scrabble.passTurnToNextPlayer();
      }
    });
    challengeMode = new SimpleBooleanProperty(false);
    challengeMode.addListener(observable -> {
      updateButtons();
      if (challengeMode.get()) {
        getAttributes().getChildren().remove(doneButton);
        getAttributes().add(challengeButton, 1, 1);
      } else {
        getAttributes().getChildren().remove(challengeButton);
        getAttributes().add(doneButton, 1, 1);
      }
    });
    frame = new Frame();
    attributesOfPlayer = new GridPane();
    doneButton = setUpDoneButton();
    passButton = setUpPassButton();
    challengeButton = setUpChallengeButton();
    setUpLabel();
    attributesOfPlayer.add(this.name, 0, 0);
    attributesOfPlayer.add(frame, 0, 1);
    attributesOfPlayer.add(doneButton, 1, 1);
    attributesOfPlayer.add(passButton, 2, 1);
    GridPane.setMargin(doneButton, new Insets(0, 15, 0, 15));
    GridPane.setMargin(challengeButton, new Insets(0, 15, 0, 15));
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
    } else if (challengeMode.get()) {
      passButton.setTextFill(Color.GREEN);
      passButton.setDisable(false);
    } else if (!isMyTurn()) {
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
      if (challengeMode.get()) {
        Scrabble.passTurnToNextChallengingPlayer(this);
      } else if (Board.getInstance().thereAreDraggableTilesOnBoard()) {
        Scrabble.logs.setText("You cannot pass turn if there are your tiles on the board.");
      } else {
        replaceAllReplacingTiles();
        Scrabble.passTurnToNextPlayer();
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

  private Button setUpChallengeButton() {
    Button challengeButton = new Button("Challenge");
    challengeButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    challengeButton.setWrapText(true);
    challengeButton.setTextOverrun(OverrunStyle.CLIP);
    challengeButton.setTextFill(Color.ORANGE);
    challengeButton.setOnAction(mouseEvent -> {
      if (Board.getInstance().checkIfThereIsWord(Board.getWord())) {
        skipNextTurn = true;
        Scrabble.logs.setText(String.format("Word: {%s} exists, player: %s skips his next turn", Board.getWord(), getName()));
        Scrabble.passTurnToNextPlayer();
      } else {
        Scrabble.logs.setText(String.format("Word: {%s} DOESN'T exists, player: %s needs to remove all tiles from board and pass his turn", Board.getWord(), Scrabble.getCurrentPlayer().getName()));
        Scrabble.getCurrentPlayer().getDoneButton().setDisable(true);
        Scrabble.getCurrentPlayer().getPassButton().setDisable(false);
        Scrabble.getCurrentPlayer().needsToRemoveAllTilesFromBoardAndSkip = true;
      }
      challengeMode.setValue(false);
    });
    return challengeButton;
  }

  private Button setUpDoneButton() {
    Button doneButton = new Button("Done");
    doneButton.setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    doneButton.setWrapText(true);
    doneButton.setTextOverrun(OverrunStyle.CLIP);
    doneButton.setOnAction(mouseEvent -> {
      if (Board.getInstance().wordPlacedCorrectly()) {
        activateChallengeMode();
/*      updateScore();
        Board.getInstance().makePlacedTilesNotDraggable();
        isMyTurn.setValue(false);
        Scrabble.getNextPlayer();*/
      } else {
        Scrabble.logs.setText("Word placed incorrectly");
      }
    });
    return doneButton;
  }

  public void setChallengeMode(boolean challengeMode) {
    this.challengeMode.set(challengeMode);
  }

  private void activateChallengeMode() {
    Scrabble.logs.setText(String.format("Word: %s", Board.getWord()));
    System.out.println(Board.getInstance().checkIfThereIsWord(Board.getWord()));
    doneButton.setDisable(true);
    passButton.setDisable(true);
    for (Player player :
            Scrabble.getPlayers()) {
      if (!player.isMyTurn()) {
        player.setChallengeMode(true);
        break;
      }
    }
  }

  private void updateScore() {
    increaseScoreBy(Board.getInstance().calculateWordScore());
    String name = this.name.getText().replaceAll("\\(\\d+\\)", "");
    this.name.setText(String.format("%s(%d)", name, score));
  }

  public boolean isMyTurn() {
    return isMyTurn.getValue();
  }

  public void setIsMyTurn(boolean isMyTurn) {
    this.isMyTurn.set(isMyTurn);
  }

  private void setUpLabel() {
    name.setTextFill(Color.BLACK);
    name.setFont(Font.font("Arial", FontWeight.BOLD, 16));
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
