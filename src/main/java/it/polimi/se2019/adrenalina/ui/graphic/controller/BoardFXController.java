package it.polimi.se2019.adrenalina.ui.graphic.controller;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardFXController {

  @FXML
  private GridPane mapGrid;
  @FXML
  private Pane playerDashboard;

  @FXML
  private ImageView weapon;

  private Pane[][] grid;

  public void initialize() {
    grid = new Pane[12][9];

    mapGrid.setVisible(false);
    for (int x = 0; x < 12; x++) {
      for (int y = 0; y < 9; y++) {
        grid[x][y] = new Pane();
        grid[x][y].setStyle("-fx-border-color: white; -fx-border-width: 1;");

        mapGrid.getChildren().add(grid[x][y]);
        GridPane.setColumnIndex(grid[x][y], x + 1);
        GridPane.setRowIndex(grid[x][y], y + 1);
      }
    }
    mapGrid.setStyle(
        "-fx-background-image: url('gui/assets/img/map1.png'); -fx-background-size: contain; -fx-background-repeat: no-repeat; -fx-background-position: center;");
    mapGrid.setVisible(true);

    Rectangle2D viewPortWeaponEDashboard = new Rectangle2D(0, 8, 350, 30);
    /*
    weapon.setViewport(viewPortWeaponEDashboard);
    weapon.setRotate(270);
    weapon.setImage(new Image("gui/assets/img/weapon/weapon_distruttore.png"));
    weapon.setPreserveRatio(true);
    */
  }
}
