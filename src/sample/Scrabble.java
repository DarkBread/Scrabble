package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Scrabble extends Application {

    static TextArea logs = new TextArea();
    private static ArrayList<Player> players;
    private static Thread gameProcess;

    static {
        players = addPlayers();
        new GameProcess().start();
        logs.setEditable(false);
        logs.setFont(Font.font("Arial", 14));
        logs.setText(logs.getText() + "Start Of The Game");
    }

    private static ArrayList<Player> addPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Patrick"));
        players.add(new Player("Bob"));
        return players;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Scrabble");
        primaryStage.getIcons().add(new Image("Resources/Scrabble.png"));
        Scene scene = new Scene(drawUI(), 975, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent drawUI() {
        //FlowPane flowPane = setUpFlowPane();
        GridPane gridPane = setUpGridPane();
        return gridPane;
        //return flowPane;
    }

    private GridPane setUpGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(50);
        GridPane.setMargin(Board.getInstance(), new Insets(20, 0, 0, 20));
        VBox framesOfPlayers = getFramesOfPlayers();
        framesOfPlayers.setSpacing(30);
        GridPane.setMargin(framesOfPlayers, new Insets(50, 0, 0, 0));
        framesOfPlayers.getChildren().add(logs);
        framesOfPlayers.getChildren().add(createConsole());
        gridPane.add(Board.getInstance(), 0, 0);
        gridPane.add(framesOfPlayers, 1, 0);
        return gridPane;
    }

    private TextField createConsole() {
        TextField console = new TextField();
        console.setPromptText("Type HELP, for help");
        console.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                switch (console.getText().toUpperCase()) {
                    case "HELP":
                        logs.setText("(not) displaying help  :)");
                        break;
                    case "QUIT":
                        Platform.exit();
                    default:
                        logs.setText("Unknown Command, try 'HELP'");
                }
            }
        });
        return console;
    }

    private FlowPane setUpFlowPane() {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(50);
        FlowPane.setMargin(Board.getInstance(), new Insets(20, 0, 0, 20));
        VBox framesOfPlayers = getFramesOfPlayers();
        framesOfPlayers.setSpacing(30);
        FlowPane.setMargin(framesOfPlayers, new Insets(50, 0, 0, 0));
        framesOfPlayers.getChildren().add(logs);
        flowPane.getChildren().addAll(Board.getInstance(), framesOfPlayers);
        return flowPane;
    }


    private VBox getFramesOfPlayers() {
        VBox framesOfPlayers = new VBox();
        for (Player player :
                players) {
            framesOfPlayers.getChildren().add(player.getAttributes());
        }
        framesOfPlayers.setSpacing(30);
        return framesOfPlayers;
    }

    private static class GameProcess extends Service {

        private static int SLEEP_TIME = 1000;
        private boolean gameHasFinished = false;

        @Override
        protected Task createTask() {
            return new Task() {
                @Override
                protected Object call() throws Exception {
                    while (!gameHasFinished) {
                        for (Player player : players) {
                            player.setIsMyTurn(true);
                            player.refillFrameWithTiles();
                            do {
                                try {
                                    Thread.sleep(SLEEP_TIME);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } while (player.isMyTurn());
                        }
                    }
                    return this;
                }
            };
        }
    }
}
