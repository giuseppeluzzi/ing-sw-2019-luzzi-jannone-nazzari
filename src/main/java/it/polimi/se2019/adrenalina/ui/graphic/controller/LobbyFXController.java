package it.polimi.se2019.adrenalina.ui.graphic.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class LobbyFXController {

  @FXML
  private BorderPane lobbyConnecting;
  @FXML
  private BorderPane lobbyConfigurationMap;
  @FXML
  private BorderPane lobbyConfigurationSkulls;

  @FXML
  private Circle map1Image;
  @FXML
  private Circle map2Image;
  @FXML
  private Circle map3Image;
  @FXML
  private Circle map4Image;
  @FXML
  private Circle skullsImage;

  public void initialize() {
    lobbyConnecting.setVisible(true);
    lobbyConfigurationMap.setVisible(false);
    lobbyConfigurationSkulls.setVisible(false);
    map1Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map1.png"), -110, -100, 233, 175, false));
    map2Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map2.png"), -110, -100, 233, 175, false));
    map3Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map3.png"), -120, -100, 233, 175, false));
    map4Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map4.png"), -110, -100, 233, 175, false));
    skullsImage.setFill(
        new ImagePattern(new Image("gui/assets/img/skulls.png"), -100, -100, 200, 200, false));
  }

  public void endLoading(boolean masterPlayer) {
    if (masterPlayer) {
      FXUtils.transition(lobbyConnecting, lobbyConfigurationMap);
    } else {
      FXUtils.transition(lobbyConnecting, null);
    }
  }

  public void nextMap(ActionEvent actionEvent) {
    FXUtils.transition(lobbyConfigurationMap, lobbyConfigurationSkulls);
  }

  public void nextSkulls(ActionEvent actionEvent) {
    FXUtils.transition(lobbyConfigurationSkulls, null);
  }
}
