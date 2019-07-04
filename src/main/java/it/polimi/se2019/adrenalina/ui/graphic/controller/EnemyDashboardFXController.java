package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class EnemyDashboardFXController extends DashboardFXController {

  @FXML
  private Pane enemyDashboardBackground;
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
  @FXML
  private Text enemyName;

  public EnemyDashboardFXController(BoardFXController boardFXController, PlayerColor playerColor) {
    super(boardFXController, playerColor);
  }

  @Override
  public Text getDashboardNameLabel() {
    return enemyName;
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
  Pane generateTag(PlayerColor color) {
    Pane pane = new Pane();
    pane.setPrefWidth(25);
    pane.setPrefHeight(27);
    pane.setMinWidth(25);
    pane.setMinHeight(27);
    pane.setStyle("-fx-background-image: url(\"gui/assets/img/tag_" + color
        + ".png\"); -fx-background-size: contain; -fx-background-repeat: no-repeat;"
        + "-fx-background-position: center;");
    return pane;
  }

  @Override
  Node generateSkull() {
    Circle circle = new Circle();
    circle.setRadius(10);
    circle.getStyleClass().add("boardSkull");
    return circle;
  }

  @Override
  public Pane getDashboardBackground() {
    return enemyDashboardBackground;
  }

  @Override
  public void updateWeapons(List<Weapon> weapons, int weaponsNum) {
    Platform.runLater(() -> {
      enemyWeapons.getChildren().clear();

      for (int i = 0; i < 3 - weaponsNum; i++) {
        ImageView placeHolder = new ImageView("gui/assets/img/weapon/rotated/weapon_back.png");
        placeHolder.setFitHeight(141);
        placeHolder.setPreserveRatio(true);
        placeHolder.setOpacity(0);
        placeHolder.setVisible(true);
        enemyWeapons.getChildren().add(placeHolder);
      }

      for (int i = 0; i < weaponsNum - weapons.size(); i++) {
        ImageView imageView = new ImageView("gui/assets/img/weapon/rotated/weapon_back.png");
        imageView.setFitHeight(141);
        imageView.setOpacity(1);
        imageView.setPreserveRatio(true);
        imageView.setVisible(true);
        enemyWeapons.getChildren().add(imageView);
      }

      for (Weapon weapon : weapons) {
        ImageView imageView = new ImageView(
            "gui/assets/img/weapon/rotated/weapon_" + weapon.getSlug() + ".png");
        imageView.setFitHeight(141);
        imageView.setOpacity(1);
        imageView.setPreserveRatio(true);
        imageView.setVisible(true);
        enemyWeapons.getChildren().add(imageView);
      }
      enemyWeapons.setVisible(true);

    });
  }

  @Override
  public void updatePowerUps(List<PowerUp> powerUps) {
    Platform.runLater(() -> {
      int remaining = powerUps.size();
      for (int i = enemyPowerUps.getChildren().size() - 1; i >= 0; i--) {
        if (remaining > 0) {
          enemyPowerUps.getChildren().get(i).setOpacity(1);
          enemyPowerUps.getChildren().get(i).setVisible(true);
          remaining--;
        } else {
          enemyPowerUps.getChildren().get(i).setOpacity(0);
          enemyPowerUps.getChildren().get(i).setVisible(true);
        }
      }
      enemyPowerUps.setVisible(true);
    });
  }

  @Override
  public void updateDashboard(PlayerColor color) {
    Platform.runLater(() -> setDashboardColor(color));
  }

  @Override
  public void setFrenzy() {
    Platform.runLater(() ->
        enemyDashboardBackground.setStyle(
            "-fx-background-image: url(\"gui/assets/img/ff_dashboard_" + getPlayerColor()
                + ".png\");"));
  }

  private void setDashboardColor(PlayerColor color) {
    enemyDashboardBackground.setStyle(
        "-fx-background-image: url(\"gui/assets/img/dashboard_" + color + ".png\");");
  }
}
