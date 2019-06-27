package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BoardFXController {

  private PlayerColor playerColor;
  private Board board;

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

  private Pane[][] grid;
  private final HashMap<PlayerColor, DashboardFXController> dashboardControllers;

  public BoardFXController() {
    dashboardControllers = new HashMap<>();
  }

  public void initialize() {
    grid = new Pane[12][9];
    /*try {
      playerColor = AppGUI.getClient().getPlayerColor();
      board = AppGUI.getClient().getBoardView().getBoard();
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

    try {
      for (Player enemy : AppGUI.getClient().getBoardView().getBoard().getPlayers()) {
        if (enemy.getColor() != playerColor) {
          loadEnemyDashboard(enemy.getColor());
        }
      }

      for (Player player : AppGUI.getClient().getBoardView().getBoard().getPlayers()) {
        dashboardControllers.get(player.getColor()).updateAmmos(player.getAmmo(AmmoColor.RED), player.getAmmo(AmmoColor.BLUE), player.getAmmo(AmmoColor.YELLOW));
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }*/

  }

  public void setMapId(int mapId) {
    Platform.runLater(() -> {
      mapGrid.setStyle(
          "-fx-background-image: url(\"gui/assets/img/map" + mapId + ".png\");");
    });
  }

  public boolean isDashboardCreated(PlayerColor color) {
    return dashboardControllers.containsKey(color);
  }

  public DashboardFXController getDashboardController(PlayerColor color)
      throws InvalidPlayerException {
    if (!dashboardControllers.containsKey(color)) {
      throw new InvalidPlayerException();
    }
    return dashboardControllers.get(color);
  }

  public void loadPlayerDashboard(PlayerColor color) {
    FXMLLoader loaderPlayerDashboard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/PlayerDashboard.fxml"));
    DashboardFXController playerDashboardFXController = new PlayerDashboardFXController(
        playerColor);
    dashboardControllers.put(playerColor, playerDashboardFXController);
    loaderPlayerDashboard.setController(playerDashboardFXController);

    Platform.runLater(() -> {
      try {
        Parent playerDashboard = loaderPlayerDashboard.load();
        playerDashboard.setId("dashboard-" + playerColor);
        GridPane.setRowIndex(playerDashboard, 1);
        GridPane.setColumnIndex(playerDashboard, 0);
        boardGrid.getChildren().add(playerDashboard);
      } catch (IOException e) {
        Log.exception(e);
      }
    });
  }

  public void loadEnemyDashboard(PlayerColor color) {
    FXMLLoader loaderEnemyDashboard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/EnemyDashboard.fxml"));

    DashboardFXController enemyDashboardFXController = new EnemyDashboardFXController(color);
    loaderEnemyDashboard.setController(enemyDashboardFXController);
    dashboardControllers.put(color, enemyDashboardFXController);

    Platform.runLater(() -> {
      try {
        Parent dashboard = loaderEnemyDashboard.load();
        dashboard.setId("dashboard-" + color);
        enemyDashboards.getChildren().add(dashboard);
      } catch (IOException e) {
        Log.exception(e);
        e.printStackTrace();
      }
    });

  }

  public void changeDashboardColor(PlayerColor from, PlayerColor to) {
    if (dashboardControllers.containsKey(from)) {
      DashboardFXController dashboardFXController = dashboardControllers.get(from);
      dashboardControllers.remove(from);
      dashboardControllers.put(to, dashboardFXController);
      dashboardFXController.setPlayerColor(to);
    }
  }

  public void unloadEnemyDashboard(PlayerColor color) {
    if (dashboardControllers.containsKey(color)) {
      dashboardControllers.remove(color);
      Platform.runLater(() -> {
        for (Node child : enemyDashboards.getChildren()) {
          if (child.getId().equals("dashboard-" + color)) {
            enemyDashboards.getChildren().remove(child);
            break;
          }
        }
      });
    }
  }
}
