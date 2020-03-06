package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

public class Scrabble extends Application {

  static TextArea logs = new TextArea();
  private static ArrayList<Player> players = new ArrayList<>();
  private static Stage gameStage;
  private static int numberOfPlayers;
  private static Label clock;

  static {
    logs.setEditable(false);
    logs.setPrefSize(300, 300);
    logs.setFont(Font.font("Arial", 17));
    logs.setText(logs.getText() + "Start Of The Game");
    logs.setWrapText(true);
  }

  public static ArrayList<Player> getPlayers() {
    return players;
  }

  private static void addPlayers(String number) {
    switch (number) {
      case "2 Players":
        numberOfPlayers = 2;
        break;
      case "3 Players":
        numberOfPlayers = 3;
        break;
      case "4 Players":
        numberOfPlayers = 4;
        break;
    }
    addPlayer();
  }


  private static void addPlayer() {
    VBox vBox = new VBox();
    vBox.setAlignment(Pos.CENTER);
    Label label = new Label("Enter player's name:");
    label.setFont(Font.font("Ariel", FontWeight.BOLD, 13));
    TextField textField = new TextField();
    textField.setFont(Font.font("Ariel", FontWeight.MEDIUM, 14));
    Button button = new Button("OK");
    textField.setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode() == KeyCode.ENTER) {
        button.fire();
      }
    });
    button.setOnAction(actionEvent -> {
      if (!textField.getText().isBlank()) {
        players.add(new Player(textField.getText()));
        if (players.size() < numberOfPlayers) {
          addPlayer();
        } else {
          Scene scene = new Scene(drawUI());
          gameStage.setScene(scene);
          gameStage.setMaximized(true);
          gameStage.show();
          players.get(0).setIsMyTurn(true);
          new Timer().start();
        }
      }
    });
    vBox.getChildren().addAll(label, textField, button);
    gameStage.setScene(new Scene(vBox, 200, 200));
    gameStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }

  static Player getCurrentPlayer() {
    Player firstPlayer = players.get(0);
    for (Player player :
            players) {
      if (player.isMyTurn()) {
        return player;
      }
    }
    return firstPlayer;
  }

  private static Parent drawUI() {
    GridPane gridPane = setUpGridPane();
    return gridPane;
  }

  private static GridPane setUpGridPane() {
    GridPane gridPane = new GridPane();
    gridPane.setHgap(25);
    VBox framesOfPlayers = getFramesOfPlayers();
    framesOfPlayers.setSpacing(30);
    framesOfPlayers.getChildren().add(logs);
    framesOfPlayers.getChildren().add(new Console());
    GridPane board = new GridPane();
    board.add(new HorizontalNavigationBar(), 1, 0);
    board.add(Board.getInstance(), 1, 1);
    board.add(new VerticalNavigationBar(), 0, 1);
    gridPane.add(board, 0, 0);
    gridPane.add(framesOfPlayers, 1, 0);
    clock = new Label("0:00");
    clock.setFont(Font.font(("Ariel"), FontWeight.BLACK, 20));
    //gridPane.add(clock, 2, 0);
    framesOfPlayers.getChildren().add(0, clock);
    GridPane.setMargin(framesOfPlayers, new Insets(10, 0, 0, 0));
    GridPane.setMargin(board, new Insets(20, 0, 0, 20));
    return gridPane;
  }

  private static VBox getFramesOfPlayers() {
    VBox framesOfPlayers = new VBox();
    for (Player player :
            players) {
      framesOfPlayers.getChildren().add(player.getAttributes());
    }
    framesOfPlayers.setSpacing(30);
    return framesOfPlayers;
  }

  public static void passTurnToNextPlayer() {
    int index = players.indexOf(getCurrentPlayer());
    getCurrentPlayer().setIsMyTurn(false);
    if (index == players.size() - 1) {
      index = 0;
    } else {
      index++;
    }
    players.get(index).setIsMyTurn(true);
  }

  public static void passTurnToNextChallengingPlayer(Player currentChallengingPlayer) {
    int index = players.indexOf(currentChallengingPlayer);
    for (int i = index; i < players.size(); i++) {
      if (players.get(i) != getCurrentPlayer() && players.get(i) != currentChallengingPlayer) {
        players.get(i).setChallengeMode(true);
        currentChallengingPlayer.setChallengeMode(false);
        return;
      }
    }
    currentChallengingPlayer.setChallengeMode(false);
    passTurnToNextPlayer();
  }

  private static void updateClock(int seconds) {
    Platform.runLater(() -> clock.setText(String.format("%d:%02d", seconds / 60, seconds % 60)));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    gameStage = primaryStage;
    primaryStage.setTitle("Scrabble");
    primaryStage.getIcons().add(new Image("Resources/Scrabble.png"));
    primaryStage.setScene(new Scene(askForHowManyPlayersWillBePlaying(), 200, 200));
    primaryStage.show();
  }

  private VBox askForHowManyPlayersWillBePlaying() {
    ComboBox<String> comboBox = new ComboBox<>();
    comboBox.getItems().addAll("2 Players", "3 Players", "4 Players", "vs AI");
    comboBox.setValue("2 Players");
    comboBox.setCellFactory(new Callback<>() {
      @Override
      public ListCell<String> call(ListView<String> param) {
        return new ListCell<>() {
          {
            super.setPrefWidth(100);
          }

          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
              setText(item);
              if (item.equals("vs AI")) {
                setDisable(true);
                setTextFill(Color.GRAY);
              }
            }
          }
        };
      }
    });
    VBox optionsOfAmountOfPlayers = new VBox();
    Label label = new Label("Choose number of players");
    label.setFont(Font.font("Ariel", FontWeight.BOLD, 12));
    Button button = new Button("OK");
    HBox hBox = new HBox(comboBox, button);
    hBox.setAlignment(Pos.CENTER);
    button.setOnAction(actionEvent -> {
      addPlayers(comboBox.getSelectionModel().getSelectedItem());
    });
    comboBox.setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode() == KeyCode.ENTER) {
        button.fire();
      }
    });
    optionsOfAmountOfPlayers.getChildren().addAll(label, hBox);
    optionsOfAmountOfPlayers.setAlignment(Pos.CENTER);
    return optionsOfAmountOfPlayers;
  }

  private static class Timer extends Service {

    private static int SLEEP_TIME = 1000;
    private boolean gameHasFinished = false;

    @Override
    protected Task createTask() {
      return new Task() {
        @Override
        protected Object call() throws Exception {
          int seconds = 0;
          do {
            try {
              Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            seconds++;
            Scrabble.updateClock(seconds);
          } while (!gameHasFinished);
          return this;
        }
      };
    }
  }
}
