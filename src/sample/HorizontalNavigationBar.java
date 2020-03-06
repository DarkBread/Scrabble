package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HorizontalNavigationBar extends HBox {

  public HorizontalNavigationBar() {
    for (int i = 1; i <= 15; i++) {
      this.setStyle("-fx-border-color: gray;-fx-border-width: 3");
      Label label = new Label(String.valueOf(i));
      label.setStyle("-fx-border-color: gray;-fx-font-weight: bold");
      label.setPrefSize(40, 20);
      label.setAlignment(Pos.CENTER);
      label.setMinHeight(20);
      getChildren().add(label);
    }
  }
}
