package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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

    private static Player getCurrentPlayer() {
        Player firstPlayer = players.get(0);
        for (Player player :
                players) {
            if (player.isMyTurn()) {
                return player;
            }
        }
        return firstPlayer;
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
                String consoleInput = console.getText().toUpperCase().trim();
                switch (consoleInput) {
                    case "HELP":
                        logs.setText("(not) displaying help  :)");
                        break;
                    case "QUIT":
                        Platform.exit();
                        break;
                    case "PASS":
                        getCurrentPlayer().getPassButton().fire();
                    default:
                        tryToParse(consoleInput);
                }
            }
        });
        return console;
    }

    private boolean tryToParse(String consoleInput) {
        String exchangeLettersPattern = "EXCHANGE( [A-Z]){1,7}";
        String placingWordPattern = "[A-O]((1[0-5])|[1-9]) (A|D|ACROSS|DOWN) ([A-Z]){1,7}";
        String[] commands = consoleInput.split(" ");
        if (consoleInput.matches(exchangeLettersPattern)) {
            checkingNextLetterInFrame:
            for (int i = 1; i < commands.length; i++) {
                for (Node node : getCurrentPlayer().getFrame().getChildren()) {
                    FrameTile frameTile = (FrameTile) node;
                    if (frameTile.getLetter() == commands[i].charAt(0) && !frameTile.isExchangeable()) {
                        frameTile.reverseExchangeable();
                        continue checkingNextLetterInFrame;
                    }
                }
                getCurrentPlayer().getFrame().makeAllTilesUnchangeable();
                logs.setText("There is no such letter(s) in your frame");
                return false;
            }
            getCurrentPlayer().getPassButton().fire();
            return true;
        }
    /*if (consoleInput.matches(placingWordPattern)) {
        int rowPosition = commands[1].charAt(0) - 'A';
        int columnPosition = Integer.parseInt(commands[1].substring(1));
        if (commands[2].charAt(0) == 'A' || commands[2].equals("ACROSS")) {
            placeWordHorizontally(Arrays.copyOfRange(commands, 3, commands.length), rowPosition, columnPosition);
        } else {
            placeWordVertically();
        }
    }*/
        return false;
    }

/*    private void placeWordHorizontally(String[] letters, int startingRow, int startingColumn) {
        for (int i = 0; i < letters.length; i++) {
            //made checks
            FrameTile frameTile = getCurrentPlayer().getFrame().getFrameTileWithLetter(letters[i].charAt(0));
            if (frameTile != null) {
                frameTile.fireEvent(new MouseEvent(MouseEvent.DRAG_DETECTED,));
            }
            Board.getInstance().getTileByRowColumnIndex(startingRow, startingColumn).fireEvent();
        }
    }*/

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
