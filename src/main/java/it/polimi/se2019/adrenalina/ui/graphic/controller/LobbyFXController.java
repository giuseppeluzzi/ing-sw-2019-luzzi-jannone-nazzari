package it.polimi.se2019.adrenalina.ui.graphic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class LobbyFXController {

  @FXML
  private BorderPane lobbyConnecting;

  public void initialize() {
    lobbyConnecting.setVisible(true);
  }

  public void endLoading(ActionEvent actionEvent) {

  }
}
