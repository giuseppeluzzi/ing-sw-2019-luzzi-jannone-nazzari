package it.polimi.se2019.adrenalina.ui.graphic.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardFXController {

  @FXML
  private GridPane mapGrid;

  private Pane[][] grid;

  public void initialize() {
    grid = new Pane[4][3];

    for (Node cell: mapGrid.getChildren()) {
      if (cell instanceof Pane) {
        int col = GridPane.getColumnIndex(cell);
        int row = GridPane.getRowIndex(cell);
        if (col != 0 && col != 5 && row != 0 && row != 4) {
          grid[col - 1][row - 1] = (Pane) cell;
          cell.setStyle("-fx-border-color: white; -fx-border-width: 1;");
        }
      }
    }

    mapGrid.setStyle("-fx-background-image: url('gui/assets/img/map1.png'); -fx-background-size: contain; -fx-background-repeat: no-repeat; -fx-background-posiition: center;");
  }
}
