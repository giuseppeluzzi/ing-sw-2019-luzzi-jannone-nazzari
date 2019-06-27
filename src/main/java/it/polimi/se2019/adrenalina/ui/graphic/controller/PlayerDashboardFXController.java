package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
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

  private final HashMap<ImageView, EventHandler<MouseEvent>> powerUpEventHandler;

  public PlayerDashboardFXController(PlayerColor playerColor) {
    super(playerColor);
    powerUpEventHandler = new HashMap<>();
  }

  public void initialize() {
    setDashboardColor(getPlayerColor());
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

  @Override
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
    Platform.runLater(() -> {
      playerWeapons.getChildren().clear();
      for (int i = 0; i < 3 - weaponsNum; i++) {
        ImageView imageView = new ImageView(
            "gui/assets/img/weapon/rotated/weapon_back.png");
        imageView.setFitHeight(182);
        imageView.setOpacity(0);
        imageView.setPreserveRatio(true);
        imageView.setVisible(true);
        playerWeapons.getChildren().add(imageView);
      }

      for (Weapon weapon : weapons) {
        ImageView imageView = new ImageView(
            "gui/assets/img/weapon/weapon_" + weapon.getSlug() + ".png");
        imageView.setFitHeight(141);
        imageView.setPreserveRatio(true);
        imageView.setVisible(true);
        imageView.setOpacity(1);
        playerWeapons.getChildren().add(imageView);
      }
      playerWeapons.setVisible(true);
      Log.debug(">>>>>>>>>>>>>>> p uw");
    });
  }

  @Override
  public void updatePowerUps(List<PowerUp> powerUps) {
    Platform.runLater(() -> {
      playerPowerUps.getChildren().clear();
      powerUpEventHandler.clear();

      for (int i = 0; i < 3 - powerUps.size(); i++) {
        ImageView imageView = new ImageView(
            "gui/assets/img/powerups/rotated/powerup_back.png");
        imageView.setFitHeight(182);
        imageView.setPreserveRatio(true);
        imageView.setOpacity(0);
        imageView.setVisible(true);
        playerPowerUps.getChildren().add(0, imageView);
      }

      ColorAdjust bnEffect = new ColorAdjust();
      bnEffect.setSaturation(-1);

      for (PowerUp powerUp : powerUps) {
        ImageView imageView = new ImageView(
            "gui/assets/img/powerups/rotated/" + powerUp.getType() + "_" + powerUp.getColor()
                + ".png");
        imageView.setFitHeight(182);
        imageView.setPreserveRatio(true);
        imageView.setVisible(true);
        imageView.setOpacity(1);
        imageView.setId(powerUp.getType().toString() + "-" + powerUp.getColor());
        imageView.setEffect(bnEffect);
        imageView.setMouseTransparent(true);

        playerPowerUps.getChildren().add(imageView);
        playerPowerUps.setVisible(true);
      }
      Log.debug(">>>>>>>>>>>>>>> p up");
    });
  }

  @Override
  public void updateDashboard(PlayerColor color) {
    Platform.runLater(() -> {
      setDashboardColor(color);
    });
  }

  private void setDashboardColor(PlayerColor color) {
    playerDashboard.setStyle(
        "-fx-background-image: url(\"gui/assets/img/dashboard_" + color + ".png\");");
  }

  public void disablePowerUps() {
    ColorAdjust bnEffect = new ColorAdjust();
    bnEffect.setSaturation(-1);

    for (Node image : getPowerUpsContainer().getChildren()) {
      if (image.getId() != null && powerUpEventHandler.containsKey(image)) {
        image.removeEventHandler(MouseEvent.MOUSE_CLICKED, powerUpEventHandler.get(image));
        powerUpEventHandler.remove(image);

        image.setMouseTransparent(true);
        image.setEffect(bnEffect);
      }
    }
  }

  public void usingPowerUp(boolean discard) {
    for (Node image : getPowerUpsContainer().getChildren()) {
      if (image.getId() != null) {
        image.setEffect(null);
        image.setMouseTransparent(false);

        EventHandler<MouseEvent> eventHandler = event -> {
          String[] powerup = image.getId().split("-");
          try {
            if (discard) {
              ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                  new PlayerDiscardPowerUpEvent(getPlayerColor(), PowerUpType.valueOf(powerup[0]),
                      AmmoColor
                          .valueOf(powerup[1])));
            } else {
              ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                  new PlayerPowerUpEvent(getPlayerColor(), PowerUpType.valueOf(powerup[0]),
                      AmmoColor
                          .valueOf(powerup[1])));
            }
            disablePowerUps();
          } catch (RemoteException e) {
            Log.exception(e);
          }
        };

        powerUpEventHandler.put((ImageView) image, eventHandler);
        image.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
      }
    }
  }
}
