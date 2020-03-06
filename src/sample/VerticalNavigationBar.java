package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class VerticalNavigationBar extends VBox {

  public VerticalNavigationBar() {
    for (int i = 0; i < 15; i++) {
      this.setStyle("-fx-border-color: gray;-fx-border-width: 3");
      Label label = new Label(String.valueOf((char) (i + 'A')));
      label.setStyle("-fx-border-color: gray;-fx-font-weight: bold;");
      label.setPrefSize(20, 40);
      setMinWidth(23);
      label.setAlignment(Pos.CENTER);
      getChildren().add(label);
    }
  }
}
