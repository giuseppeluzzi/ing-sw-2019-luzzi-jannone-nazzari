package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class PlayerDashboardFXController extends DashboardFXController {

  @FXML
  private GridPane playerDashboard;
  @FXML
  private HBox playerTagsContainer;
  @FXML
  private HBox playerDamagesContainer;
  @FXML
  private HBox playerSkulls;
  @FXML
  private FlowPane playerAmmos;
  @FXML
  private HBox playerWeapons;
  @FXML
  private HBox playerPowerUps;

  public PlayerDashboardFXController(PlayerColor playerColor) {
    super(playerColor);
  }

  public void initialize() {
    playerDashboard.setStyle(
        "-fx-background-image: url(\"gui/assets/img/dashboard_" + getPlayerColor() + ".png\");");
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
  HBox getWeaponContainer() {
    return playerWeapons;
  }

  HBox getPowerUpsContainer() {
    return playerPowerUps;
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

  @Override
  public void updateWeapons(List<Weapon> weapons, int weaponsNum) {
    for (Weapon weapon : weapons) {
      ImageView imageView = new ImageView(
          "gui/assets/img/weapon/weapon_" + weapon.getSlug() + ".png");
      imageView.setFitHeight(141);
      imageView.setPreserveRatio(true);
      playerWeapons.getChildren().add(0, imageView);
    }
  }

  public void updatePowerUps(List<PowerUp> powerUps) {
    for (PowerUp powerUp : powerUps) {
      ImageView imageView = new ImageView(
          "gui/assets/img/powerups/" + powerUp.getType() + "_" + powerUp.getColor() + ".png");
      imageView.setFitHeight(141);
      imageView.setPreserveRatio(true);
      imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
        // TODO
        try {
          ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
              new PlayerDiscardPowerUpEvent(AppGUI.getClient().getPlayerColor(), powerUp.getType(),
                  powerUp.getColor()));
        } catch (RemoteException e) {
          Log.exception(e);
        }
      });
      playerPowerUps.getChildren().add(0, imageView);
    }
  }
}
