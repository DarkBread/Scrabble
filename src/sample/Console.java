package sample;


import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Console extends TextField {

  final static String RULES = "src/Resources/Rules.txt";
  final static String HELP = "src/Resources/Help";

  public Console() {
    setPromptText("Type HELP, for help");
    setFont(Font.font("Ariel", FontWeight.BOLD, 14));
    setOnKeyPressed(keyEvent -> {
      if (keyEvent.getCode().equals(KeyCode.ENTER)) {
        String consoleInput = getText().toUpperCase().trim();
        switch (consoleInput) {
          case "RULES":
            Scrabble.logs.setText(readRules(RULES));
            break;
          case "HELP":
            Scrabble.logs.setText(readRules(HELP));
            break;
          case "QUIT":
            Platform.exit();
            break;
          case "PASS":
            Scrabble.getCurrentPlayer().getPassButton().fire();
          default:
            tryToParse(consoleInput);
        }
      }
    });
  }

  private String readRules(String filePath) {
    StringBuilder stringBuilder = null;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      String line;
      stringBuilder = new StringBuilder();
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (stringBuilder != null) {
      return stringBuilder.toString();
    } else {
      return "";
    }
  }
  private static boolean tryToParse(String consoleInput) {
    String exchangeLettersPattern = "EXCHANGE( [A-Z]){1,7}";
    String placingWordPattern = "[A-O]((1[0-5])|[1-9]) (A|D|ACROSS|DOWN) ([A-Z]){1,7}";
    String[] commands = consoleInput.split(" ");
    if (consoleInput.matches(exchangeLettersPattern)) {
      checkingNextLetterInFrame:
      for (int i = 1; i < commands.length; i++) {
        for (Node node : Scrabble.getCurrentPlayer().getFrame().getChildren()) {
          FrameTile frameTile = (FrameTile) node;
          if (frameTile.getLetter() == commands[i].charAt(0) && !frameTile.isExchangeable()) {
            frameTile.reverseExchangeable();
            continue checkingNextLetterInFrame;
          }
        }
        Scrabble.getCurrentPlayer().getFrame().makeAllTilesUnchangeable();
        Scrabble.logs.setText("There is no such letter(s) in your frame");
        return false;
      }
      Scrabble.getCurrentPlayer().getPassButton().fire();
      return true;
    } else if (consoleInput.matches(placingWordPattern)) {
      int rowPosition = commands[0].charAt(0) - 'A';
      int columnPosition = Integer.parseInt(commands[0].substring(1)) - 1;
      if (commands[1].charAt(0) == 'A' || commands[1].equals("ACROSS")) {
        Board.placeWordHorizontally(commands[2], rowPosition, columnPosition);
      } else {
        Board.placeWordVertically(commands[2], rowPosition, columnPosition);
      }
    }
    return false;
  }
}
