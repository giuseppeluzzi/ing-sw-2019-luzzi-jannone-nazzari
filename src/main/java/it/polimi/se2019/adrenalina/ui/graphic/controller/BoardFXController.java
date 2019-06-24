package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BoardFXController {

  @FXML
  private GridPane mapGrid;
  @FXML
  private Pane playerDashboardContainer;
  @FXML
  private VBox enemyDashboards;

  @FXML
  private Pane weapon;

  private Pane[][] grid;

  public void initialize() {
    grid = new Pane[12][9];

    mapGrid.setVisible(false);
    mapGrid.setStyle("-fx-background-image: url('gui/assets/img/map1.png');");

    for (int x = 0; x < 12; x++) {
      for (int y = 0; y < 9; y++) {
        grid[x][y] = new Pane();
        grid[x][y].setStyle("-fx-border-color: white; -fx-border-width: 1;");

        mapGrid.getChildren().add(grid[x][y]);
        GridPane.setColumnIndex(grid[x][y], x + 1);
        GridPane.setRowIndex(grid[x][y], y + 1);
      }
    }

    mapGrid.setVisible(true);
  }
}
