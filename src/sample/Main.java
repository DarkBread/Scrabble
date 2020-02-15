package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private VBox vbox;


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Scrabble");
        Scene scene = new Scene(drawUI(), 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
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
        flowPane.getChildren().addAll(Board.getInstance(), framesOfPlayers);
        return flowPane;
    }

    private VBox getFramesOfPlayers() {
        Player one = new Player("Bob");
        Player two = new Player("Patrick");
        VBox framesOfPlayers = new VBox(one.getFrame(), two.getFrame());
        framesOfPlayers.setSpacing(30);
        return framesOfPlayers;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
