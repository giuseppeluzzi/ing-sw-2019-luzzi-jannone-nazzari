package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class PlayerDashboardFXController extends DashboardFXController {

  @FXML
  private HBox playerTagsContainer;
  @FXML
  private HBox playerDamagesContainer;
  @FXML
  private HBox playerSkulls;
  @FXML
  private FlowPane playerAmmos;

  public void initialize() {

  }

  @Override
  HBox getDamagesContainer() {
    return playerDamagesContainer;
  }

  @Override
  HBox getTagsContainer() {
    return playerTagsContainer;
  }

  @Override
  HBox getSkullsContainer() {
    return playerSkulls;
  }

  @Override
  FlowPane getAmmosContainer() {
    return playerAmmos;
  }

  @Override
  Pane generateSkull() {
    Pane pane = new Pane();
    pane.setPrefHeight(32);
    pane.setPrefWidth(32);
    pane.setStyle("-fx-background-color: blue;");
    return pane;
  }

  @Override
  Pane generateTag(PlayerColor color) {
    Pane pane = new Pane();
    pane.setPrefHeight(32);
    pane.setPrefWidth(32);
    pane.setStyle("-fx-background-color: " + color.getHexColor() + ";");
    return pane;
  }

}
