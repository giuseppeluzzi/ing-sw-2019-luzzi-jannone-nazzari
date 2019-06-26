package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class BoardFXController {

  private PlayerColor playerColor;

  @FXML
  private GridPane boardGrid;

  @FXML
  private GridPane mapGrid;
  @FXML
  private Pane playerDashboardContainer;
  @FXML
  private VBox enemyDashboards;

  @FXML
  private Pane weapon;

  private TilePane[][] grid;
  private final HashMap<PlayerColor, DashboardFXController> dashboardControllers;

  public BoardFXController() {
    dashboardControllers = new HashMap<>();
  }

  public void initialize() {
    grid = new TilePane[4][3];

    mapGrid.setVisible(false);
    mapGrid.setStyle(
        "-fx-background-image: url(\"gui/assets/img/map" + AppGUI.getLobbyFXController().getMapId()
            + ".png\");");

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        grid[x][y] = new TilePane();
        grid[x][y].setStyle("-fx-border-color: white; -fx-border-width: 1;");

        mapGrid.getChildren().add(grid[x][y]);
        GridPane.setColumnIndex(grid[x][y], x + 1);
        GridPane.setRowIndex(grid[x][y], y + 1);
      }
    }

    mapGrid.setVisible(true);

    try {
      playerColor = AppGUI.getClient().getPlayerColor();
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    }

    FXMLLoader loaderPlayerDashboard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/PlayerDashboard.fxml"));
    DashboardFXController playerDashboardFXController = new PlayerDashboardFXController(
        playerColor);
    dashboardControllers.put(playerColor, playerDashboardFXController);
    loaderPlayerDashboard.setController(playerDashboardFXController);

    try {
      Parent playerDashboard = loaderPlayerDashboard.load();
      GridPane.setRowIndex(playerDashboard, 1);
      GridPane.setColumnIndex(playerDashboard, 0);

      boardGrid.getChildren().add(playerDashboard);
    } catch (IOException e) {
      Log.exception(e);
    }

    FXMLLoader loaderEnemyDashboard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/EnemyDashboard.fxml"));
    try {
      for (Player enemy : AppGUI.getClient().getBoardView().getBoard().getPlayers()) {
        if (enemy.getColor() != playerColor) {
          DashboardFXController enemyDashboardFXController = new EnemyDashboardFXController(
              enemy.getColor());
          loaderEnemyDashboard.setController(enemyDashboardFXController);
          dashboardControllers.put(enemy.getColor(), enemyDashboardFXController);
          enemyDashboards.getChildren().add(loaderEnemyDashboard.load());
        }
      }
    } catch (RemoteException e) {
      Log.exception(e);
    } catch (IOException e) {
      Log.exception(e);
      e.printStackTrace();
    }
  }

  public DashboardFXController getDashboardController(PlayerColor color)
      throws InvalidPlayerException {
    if (! dashboardControllers.containsKey(color)) {
      throw new InvalidPlayerException();
    }
    return dashboardControllers.get(color);
  }

  public void setAmmoCard(int posX, int posY, String ammoCardStr) {
    if (ammoCardStr == null) {
      Node toRemove = null;
      for (Node node : grid[posX][posY].getChildren()) {
        if (node.getStyleClass().contains("ammoCard")) {
          toRemove = node;
          break;
        }
      }
      if (toRemove != null) {
        grid[posX][posY].getChildren().remove(toRemove);
      }
    } else {
      ImageView imageView = new ImageView(String.format("gui/assets/img/ammo/ammo_%s.png", ammoCardStr));
      imageView.getStyleClass().add("ammoCard");
      grid[posX][posY].getChildren().add(imageView);
    }
  }
}
