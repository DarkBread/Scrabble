package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Scrabble extends Application {

    public static TextArea logs = new TextArea();
    private static ArrayList<Player> players;
    private static Thread gameProcess;

    static {
        players = addPlayers();
        Runnable game = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (Player player : players) {
                        player.setIsMyTurn(true);
                        player.refillFrameWithTiles();
                        do {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (player.isMyTurn());
                    }
                }
            }
        };
        gameProcess = new Thread(game);
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
        Scene scene = new Scene(drawUI(), 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }

    private void startGame() {
        gameProcess.start();
    }

    private Parent drawUI() {
        FlowPane flowPane = setUpFlowPane();
        return flowPane;
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
}
