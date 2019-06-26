package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
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

  public EnemyDashboardFXController(PlayerColor playerColor) {
    super(playerColor);
  }


  public void initialize() {
    enemyDashboard.setStyle(
        "-fx-background-image: url(\"gui/assets/img/dashboard_" + getPlayerColor() + ".png\");");
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
    for (Weapon weapon : weapons) {
      ImageView imageView = new ImageView("gui/assets/img/weapon/weapon_" + weapon.getSlug() + ".png");
      imageView.setFitHeight(141);
      imageView.setPreserveRatio(true);
      enemyWeapons.getChildren().add(0, imageView);
    }

    for (int i = 0; i < 3 - weaponsNum; i++) {
      ImageView imageView = new ImageView("gui/assets/img/weapon/weapon_back.png");
      imageView.setFitHeight(141);
      imageView.setPreserveRatio(true);
      enemyWeapons.getChildren().add(0, imageView);
    }
  }
}
