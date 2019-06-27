package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class EnemyDashboardFXController extends DashboardFXController {

  @FXML
  private GridPane enemyDashboard;
  @FXML
  private HBox enemyTagsContainer;
  @FXML
  private HBox enemyDamagesContainer;
  @FXML
  private HBox enemySkulls;
  @FXML
  private FlowPane enemyAmmos;
  @FXML
  private HBox enemyWeapons;
  @FXML
  private HBox enemyPowerUps;

  public EnemyDashboardFXController(PlayerColor playerColor) {
    super(playerColor);
  }

  public void initialize() {
    setDashboardColor(getPlayerColor());
  }

  @Override
  HBox getDamagesContainer() {
    return enemyDamagesContainer;
  }

  @Override
  HBox getTagsContainer() {
    return enemyTagsContainer;
  }

  @Override
  HBox getSkullsContainer() {
    return enemySkulls;
  }

  @Override
  FlowPane getAmmosContainer() {
    return enemyAmmos;
  }

  @Override
  HBox getWeaponContainer() {
    return enemyWeapons;
  }

  @Override
  HBox getPowerUpsContainer() {
    return enemyPowerUps;
  }

  @Override
  Pane generateTag(PlayerColor damage) {
    Pane pane = new Pane();
    pane.getStyleClass().add("enemySkull");
    return pane;
  }

  @Override
  Pane generateSkull() {
    Pane pane = new Pane();
    pane.getStyleClass().add("enemyDamage");
    return pane;
  }

  @Override
  public void updateWeapons(List<Weapon> weapons, int weaponsNum) {
    Platform.runLater(() -> {
      enemyWeapons.getChildren().clear();

      for (int i = 0; i < 3 - weapons.size() - weaponsNum; i++) {
        ImageView placeHolder = new ImageView("gui/assets/img/weapon/rotated/weapon_back.png");
        placeHolder.setFitHeight(141);
        placeHolder.setPreserveRatio(true);
        placeHolder.setOpacity(0);
        enemyWeapons.getChildren().add(placeHolder);
      }

      for (int i = 0; i < weaponsNum - weapons.size(); i++) {
        ImageView imageView = new ImageView("gui/assets/img/weapon/rotated/weapon_back.png");
        imageView.setFitHeight(141);
        imageView.setPreserveRatio(true);
        enemyWeapons.getChildren().add(imageView);
      }

      for (Weapon weapon : weapons) {
        ImageView imageView = new ImageView(
            "gui/assets/img/weapon/rotated/weapon_" + weapon.getSlug() + ".png");
        imageView.setFitHeight(141);
        imageView.setPreserveRatio(true);
        enemyWeapons.getChildren().add(imageView);
      }

    });
  }

  @Override
  public void updatePowerUps(List<PowerUp> powerUps) {
    Platform.runLater(() -> {
      int remaining = powerUps.size();
      for (int i = enemyPowerUps.getChildren().size() - 1; i >= 0; i--) {
        if (remaining > 0) {
          enemyPowerUps.getChildren().get(i).setOpacity(1);
          remaining--;
        } else {
          enemyPowerUps.getChildren().get(i).setOpacity(0);
        }
      }
    });
  }

  @Override
  public void updateDashboard(PlayerColor color) {
    Platform.runLater(() -> setDashboardColor(color));
  }

  private void setDashboardColor(PlayerColor color) {
    enemyDashboard.setStyle(
        "-fx-background-image: url(\"gui/assets/img/dashboard_" + getPlayerColor() + ".png\");");
  }
}
