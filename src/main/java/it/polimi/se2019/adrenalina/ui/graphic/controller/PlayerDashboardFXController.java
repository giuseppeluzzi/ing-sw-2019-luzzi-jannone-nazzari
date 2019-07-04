package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class PlayerDashboardFXController extends DashboardFXController {

  @FXML
  private Pane playerDashboardBackground;

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
  @FXML
  private Text playerName;

  private final EventHandler<MouseEvent> selectWeaponEventHandler;
  private final EventHandler<MouseEvent> selectPowerUpEventHandler;
  private final EventHandler<MouseEvent> discardPowerUpEventHandler;
  private final EventHandler<MouseEvent> weaponTranslateEnterEventHandler;
  private final EventHandler<MouseEvent> weaponTranslateExitEventHandler;
  private EventHandler<MouseEvent> powerUpEventHandler;

  private static final String PROP_WEAPON = "weapon";
  private static final String PROP_POWERUP = "powerup";

  public PlayerDashboardFXController(BoardFXController boardFXController, PlayerColor playerColor) {
    super(boardFXController, playerColor);

    selectWeaponEventHandler = event -> {
      boardFXController.stopTurnTimer();
      final String weaponName = ((Weapon) ((Node) event.getSource()).getProperties()
          .get(PROP_WEAPON))
          .getName();

      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
            new PlayerSelectWeaponEvent(AppGUI.getClient().getPlayerColor(), weaponName));
      } catch (RemoteException e) {
        Log.exception(e);
      }

      disableWeapons();
    };

    selectPowerUpEventHandler = event -> {
      boardFXController.stopTurnTimer();
      PowerUp powerup = (PowerUp) ((Node) event.getSource()).getProperties().get(PROP_POWERUP);

      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
            new PlayerPowerUpEvent(AppGUI.getClient().getPlayerColor(), powerup.getType(),
                powerup.getColor()));
      } catch (RemoteException e) {
        Log.exception(e);
      }

      disablePowerUps();
      boardFXController.hidePowerUpSkip();
    };

    discardPowerUpEventHandler = event -> {
      boardFXController.stopTurnTimer();
      PowerUp powerup = (PowerUp) ((Node) event.getSource()).getProperties().get(PROP_POWERUP);

      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
            new PlayerDiscardPowerUpEvent(AppGUI.getClient().getPlayerColor(), powerup.getType(),
                powerup.getColor()));
      } catch (RemoteException e) {
        Log.exception(e);
      }

      disablePowerUps();
      boardFXController.hidePowerUpSkip();
    };

    weaponTranslateEnterEventHandler = event -> ((Node) event.getSource()).setTranslateX(-275);

    weaponTranslateExitEventHandler = event -> ((Node) event.getSource()).setTranslateX(0);
  }

  @Override
  public Text getDashboardNameLabel() {
    return playerName;
  }

  public void initialize() {
    setDashboardColor(getPlayerColor());
    for (Node weapon : getWeaponContainer().getChildren()) {
      weapon.addEventHandler(MouseEvent.MOUSE_ENTERED, weaponTranslateEnterEventHandler);
      weapon.addEventHandler(MouseEvent.MOUSE_EXITED, weaponTranslateExitEventHandler);
    }
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
  Node generateSkull() {
    Circle circle = new Circle();
    circle.getStyleClass().add("boardSkull");
    circle.setRadius(13);
    return circle;
  }

  @Override
  Pane generateTag(PlayerColor color) {
    Pane pane = new Pane();
    pane.setPrefHeight(37);
    pane.setPrefWidth(34);
    pane.setMinHeight(37);
    pane.setMinWidth(34);
    pane.setStyle("-fx-background-image: url(\"gui/assets/img/tag_" + color
        + ".png\"); -fx-background-size: contain; -fx-background-repeat: no-repeat; "
        + " -fx-background-position: center;");
    return pane;
  }

  @Override
  public void updateWeapons(List<Weapon> weapons, int weaponsNum) {
    Platform.runLater(() -> {
      ColorAdjust bnEffect = new ColorAdjust();
      bnEffect.setSaturation(-1);

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
            "gui/assets/img/weapon/rotated/weapon_" + weapon.getSlug() + ".png");
        imageView.setFitHeight(182);
        imageView.setPreserveRatio(true);
        imageView.setOpacity(1);
        imageView.getProperties().put(PROP_WEAPON, weapon);
        imageView.setEffect(bnEffect);
        imageView.addEventHandler(MouseEvent.MOUSE_ENTERED, BoardFXController::handlePlayerWeaponHoverIn);
        imageView.addEventHandler(MouseEvent.MOUSE_EXITED, BoardFXController::handlePlayerWeaponHoverOut);
        playerWeapons.getChildren().add(imageView);
      }
      playerWeapons.setVisible(true);
    });
  }

  @Override
  public void updatePowerUps(List<PowerUp> powerUps) {
    Platform.runLater(() -> {
      playerPowerUps.getChildren().clear();

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
        imageView.getProperties().put(PROP_POWERUP, powerUp);
        imageView.setEffect(bnEffect);

        playerPowerUps.getChildren().add(imageView);
        playerPowerUps.setVisible(true);
      }
    });
  }

  @Override
  public void updateDashboard(PlayerColor color) {
    Platform.runLater(() -> setDashboardColor(color));
  }

  @Override
  public void setFrenzy() {
    Platform.runLater(() ->
        playerDashboard.setStyle(
            "-fx-background-image: url(\"gui/assets/img/ff_dashboard_" + getPlayerColor()
                + ".png\");"));
  }

  private void setDashboardColor(PlayerColor color) {
    playerDashboard.setStyle(
        "-fx-background-image: url(\"gui/assets/img/dashboard_" + color + ".png\");");
  }

  public void disablePowerUps() {
    getBoardFXController().setHelpText("");

    ColorAdjust bnEffect = new ColorAdjust();
    bnEffect.setSaturation(-1);

    for (Node powerup : getPowerUpsContainer().getChildren()) {
      if (powerup.getProperties().containsKey(PROP_POWERUP)) {
        Platform.runLater(() -> powerup.setEffect(bnEffect));
        powerup.removeEventHandler(MouseEvent.MOUSE_CLICKED, powerUpEventHandler);
      }
    }
  }

  public void disableWeapons() {
    getBoardFXController().setHelpText("");

    ColorAdjust bnEffect = new ColorAdjust();
    bnEffect.setSaturation(-1);

    for (Node weapon : getWeaponContainer().getChildren()) {
      if (weapon.getProperties().containsKey(PROP_WEAPON)) {
        Platform.runLater(() -> weapon.setEffect(bnEffect));
        weapon.removeEventHandler(MouseEvent.MOUSE_CLICKED, selectWeaponEventHandler);
      }
    }
  }

  public void usingPowerUp(List<PowerUp> powerUps, boolean discard, String targetName) {
    if (discard) {
      powerUpEventHandler = discardPowerUpEventHandler;
      getBoardFXController().setHelpText("Seleziona un potenziamento da scartare");
    } else {
      powerUpEventHandler = selectPowerUpEventHandler;
      if (targetName != null) {
        getBoardFXController()
            .setHelpText("Seleziona un potenziamento da utilizzare contro " + targetName);
      } else {
        getBoardFXController().setHelpText("Seleziona un potenziamento da utilizzare");
      }
      getBoardFXController().showPowerUpSkip();
    }

    for (Node image : getPowerUpsContainer().getChildren()) {
      if (image.getProperties().containsKey(PROP_POWERUP) && powerUps
          .contains(image.getProperties().get(PROP_POWERUP))) {
        Platform.runLater(() -> image.setEffect(null));

        image.addEventHandler(MouseEvent.MOUSE_CLICKED, powerUpEventHandler);
      }
    }
  }

  public void usingWeapon(List<Weapon> weapons) {
    getBoardFXController().setHelpText("Seleziona un'arma da utilizzare");

    for (Node weapon : playerWeapons.getChildren()) {
      if (weapon.getProperties().containsKey(PROP_WEAPON) && weapons
          .contains(weapon.getProperties().get(PROP_WEAPON))) {
        Platform.runLater(() -> weapon.setEffect(null));
        weapon.addEventHandler(MouseEvent.MOUSE_CLICKED, selectWeaponEventHandler);
      }
    }
  }
}