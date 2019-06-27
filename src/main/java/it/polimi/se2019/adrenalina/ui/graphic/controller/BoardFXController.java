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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

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

  private TilePane[][] grid;
  private final HashMap<PlayerColor, DashboardFXController> dashboardControllers;

  public BoardFXController() {
    dashboardControllers = new HashMap<>();
  }

  public void initialize() {
    grid = new TilePane[4][3];

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        grid[x][y] = new TilePane();
        grid[x][y].setStyle("-fx-border-color: white; -fx-border-width: 1;");
        grid[x][y].setAlignment(Pos.CENTER);
        grid[x][y].setHgap(10);
        grid[x][y].setVgap(10);

        mapGrid.getChildren().add(grid[x][y]);
        GridPane.setColumnIndex(grid[x][y], x + 1);
        GridPane.setRowIndex(grid[x][y], y + 1);
      }
    }

    /*mapGrid.setVisible(true);

    try {
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

  public void setAmmoCard(int posX, int posY, String ammoCardStr) {
    Log.println(posX + " " + posY + " " + ammoCardStr);
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
      imageView.setPreserveRatio(true);
      imageView.setFitWidth(35);
      grid[posX][posY].getChildren().add(imageView);
    }
  }

  public void setPlayerPosition(int posX, int posY, PlayerColor playerColor) {
    int removeX = -1;
    int removeY = -1;
    Node toRemove = null;
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        for (Node node : grid[x][y].getChildren()) {
          if (node.getStyleClass().contains("player_" + playerColor.name()))  {
            removeX = x;
            removeY = y;
            toRemove = node;
            break;
          }
        }
        if (toRemove != null) { break; }
      }
      if (toRemove != null) { break; }
    }
    if (toRemove != null) {
      grid[removeX][removeY].getChildren().remove(toRemove);
    }
    Circle playerIcon = new Circle(13, Color.web(playerColor.getHexColor()));
    playerIcon.getStyleClass().add("player");
    playerIcon.getStyleClass().add("player_" + playerColor.name());
    playerIcon.setStroke(Color.WHITE);
    playerIcon.setStrokeWidth(1);
    grid[posX][posY].getChildren().add(playerIcon);
  }
}
