package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectSquareEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SkipSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SquareMoveSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Kill;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class BoardFXController {

  @FXML
  private GridPane boardGrid;

  @FXML
  private GridPane mapGrid;
  @FXML
  private Region spacerBottomGrid;
  @FXML
  private HBox bottomGrid;
  @FXML
  private Pane playerDashboardContainer;
  @FXML
  private VBox enemyDashboards;
  @FXML
  private Button skipTurnActionButton;
  @FXML
  private Button noPowerUpTurnActionButton;
  @FXML
  private VBox turnActionButtons;

  @FXML
  private VBox redWeapons;
  @FXML
  private VBox redWeaponsHover;
  @FXML
  private HBox blueWeapons;
  @FXML
  private HBox blueWeaponsHover;
  @FXML
  private VBox yellowWeapons;
  @FXML
  private VBox yellowWeaponsHover;
  @FXML
  private VBox boardHasWeapons;
  @FXML
  private HBox normalBoardSkulls;
  @FXML
  private FlowPane dominationBoardSkulls;
  @FXML
  private HBox dominationKilltrack;
  @FXML
  private HBox blueSpawnPointDamages;
  @FXML
  private HBox redSpawnPointDamages;
  @FXML
  private HBox yellowSpawnPointDamages;

  private Text helperText;
  private Pane boardSkulls;

  private boolean domination;
  private GUIGridSquare[][] grid;

  private final EnumMap<PlayerColor, GUIPlayerTile> playerTiles;
  private final EnumMap<PlayerColor, DashboardFXController> dashboardControllers;
  private final HashMap<String, ImageView> squareWeapons;
  private final HashMap<String, ImageView> squareWeaponsHover;
  private EventHandler<ActionEvent> skipEventHandler;

  private final EventHandler<MouseEvent> buyWeaponEventHandler;

  public BoardFXController() {
    dashboardControllers = new EnumMap<>(PlayerColor.class);
    playerTiles = new EnumMap<>(PlayerColor.class);
    squareWeapons = new HashMap<>();
    squareWeaponsHover = new HashMap<>();

    buyWeaponEventHandler = event -> {
      final String weaponName = ((Weapon) ((Node) event.getSource()).getProperties().get("weapon"))
          .getName();

      disableBoardWeapons();

      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
            new PlayerCollectWeaponEvent(AppGUI.getClient().getPlayerColor(), weaponName));
      } catch (RemoteException e) {
        Log.exception(e);
      }
    };
  }

  public void initialize() {
    grid = new GUIGridSquare[4][3];

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        TilePane cellTilePane = new TilePane();
        cellTilePane.setStyle("-fx-border-color: white; -fx-border-width: 1;");
        cellTilePane.setAlignment(Pos.CENTER);
        cellTilePane.setHgap(10);
        cellTilePane.setVgap(10);

        Pane cellHoverPane = new Pane();
        cellHoverPane.getProperties().put("x", x);
        cellHoverPane.getProperties().put("y", y);
        cellHoverPane.getStyleClass().add("disabledSquare");
        cellHoverPane.setVisible(false);

        StackPane cellStackPane = new StackPane(cellTilePane, cellHoverPane);
        mapGrid.getChildren().add(0, cellStackPane);
        GridPane.setColumnIndex(cellStackPane, x + 1);
        GridPane.setRowIndex(cellStackPane, y + 1);

        grid[x][y] = new GUIGridSquare(x, y, cellTilePane, cellHoverPane);
      }
    }

    HBox.setHgrow(spacerBottomGrid, Priority.ALWAYS);

    mapGrid.setStyle(
        "-fx-background-image: url(\"gui/assets/img/map1.png\");");

    noPowerUpTurnActionButton.setOnAction(event -> {
      try {
        ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
            new PlayerPowerUpEvent(AppGUI.getClient().getPlayerColor(), null, null));
        hidePowerUpSkip();
      } catch (RemoteException e) {
        Log.exception(e);
      }
    });
  }

  public void setMapId(int mapId) {
    Platform.runLater(() ->
        mapGrid.setStyle(
            "-fx-background-image: url(\"gui/assets/img/map" + mapId + ".png\");")
    );
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
    PlayerDashboardFXController playerDashboardFXController = new PlayerDashboardFXController(
        color);
    dashboardControllers.put(color, playerDashboardFXController);
    loaderPlayerDashboard.setController(playerDashboardFXController);

    try {
      domination = AppGUI.getClient().isDomination();
    } catch (RemoteException e) {
      Log.exception(e);
    }

    if (domination) {
      loadDominationInterface();
    } else {
      boardSkulls = normalBoardSkulls;
    }
    loadBoardSkulls();

    Platform.runLater(() -> {
      try {
        Parent playerDashboard = loaderPlayerDashboard.load();
        helperText = playerDashboardFXController.getHelperText();
        playerDashboard.getProperties().put("color", color);
        HBox.setHgrow(playerDashboard, Priority.NEVER);
        bottomGrid.getChildren().add(0, playerDashboard);
      } catch (IOException e) {
        Log.exception(e);
      }
    });
  }

  private void loadBoardSkulls() {
    int skulls = 0;

    try {
      skulls = AppGUI.getClient().getBoardView().getBoard().getSkulls();
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    }

    for (int i = 0; i < skulls; i++) {
      Circle skull = new Circle();
      skull.getStyleClass().add("boardSkull");
      boardSkulls.getChildren().add(skull);
    }
  }

  private void loadDominationInterface() {
    normalBoardSkulls.setVisible(false);
    dominationBoardSkulls.setVisible(true);
    boardSkulls = dominationBoardSkulls;

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
        dashboard.getProperties().put("color", color);
        enemyDashboards.getChildren().add(dashboard);
      } catch (IOException e) {
        Log.exception(e);
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
          if (color == child.getProperties().get("color")) {
            enemyDashboards.getChildren().remove(child);
            break;
          }
        }
      });
    }
  }

  public void setAmmoCard(int posX, int posY, String ammoCardStr) {
    if (ammoCardStr == null) {
      Node toRemove = null;
      for (Node node : grid[posX][posY].getTilePane().getChildren()) {
        if (node.getStyleClass().contains("ammoCard")) {
          toRemove = node;
          break;
        }
      }
      if (toRemove != null) {
        grid[posX][posY].getTilePane().getChildren().remove(toRemove);
      }
    } else {
      ImageView imageView = new ImageView(
          String.format("gui/assets/img/ammo/ammo_%s.png", ammoCardStr));
      imageView.getStyleClass().add("ammoCard");
      imageView.setPreserveRatio(true);
      imageView.setFitWidth(35);
      grid[posX][posY].getTilePane().getChildren().add(0, imageView);
    }
  }

  public void setPlayerPosition(int posX, int posY, PlayerColor playerColor) {
    int removeX = -1;
    int removeY = -1;
    Node toRemove = null;
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        for (Node node : grid[x][y].getTilePane().getChildren()) {
          if (node.getProperties().containsKey("playerColor")
              && node.getProperties().get("playerColor") == playerColor) {
            removeX = x;
            removeY = y;
            toRemove = node;
            break;
          }
        }
        if (toRemove != null) {
          break;
        }
      }
      if (toRemove != null) {
        break;
      }
    }
    if (toRemove != null) {
      grid[removeX][removeY].getTilePane().getChildren().remove(toRemove);
      playerTiles.remove(playerColor);
    }
    Circle playerIcon = new Circle(13, Color.web(playerColor.getHexColor()));
    playerIcon.getStyleClass().add("player");
    playerIcon.getProperties().put("playerColor", playerColor);
    playerIcon.setStroke(Color.WHITE);
    playerIcon.setStrokeWidth(1);
    grid[posX][posY].getTilePane().getChildren().add(0, playerIcon);

    if (playerTiles.containsKey(playerColor)) {
      playerTiles.get(playerColor).setPlayerIcon(playerIcon);
    } else {
      playerTiles.put(playerColor, new GUIPlayerTile(playerColor, playerIcon));
    }
  }

  public void showTurnActions(List<TurnAction> turnActions) {
    Platform.runLater(() -> {
      for (TurnAction turnAction : turnActions) {
        Button button = new Button(turnAction.getName());
        button.setTooltip(new Tooltip(turnAction.getDescription()));
        button.setPrefWidth(300);

        button.setOnAction(event -> {
          try {
            ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new PlayerActionSelectionEvent(AppGUI.getClient().getPlayerColor(), turnAction));
          } catch (RemoteException e) {
            Log.exception(e);
          }
          clearTurnActions();
        });

        turnActionButtons.getChildren().add(button);
      }
    });
  }

  public void clearTurnActions() {
    Platform.runLater(() -> turnActionButtons.getChildren().clear());
  }

  public void showSkip() {
    Platform.runLater(() -> skipTurnActionButton.setVisible(true));
  }

  public void hideSkip() {
    Platform.runLater(() -> skipTurnActionButton.setVisible(false));
  }

  public void showPowerUpSkip() {
    Platform.runLater(() -> noPowerUpTurnActionButton.setVisible(true));
  }

  public void hidePowerUpSkip() {
    Platform.runLater(() -> noPowerUpTurnActionButton.setVisible(false));
  }

  /**
   * Removes the selectable view from the map grid
   */
  public void disableTargetSelection() {
    hideSkip();

    if (skipEventHandler != null) {
      skipTurnActionButton.setOnAction(null);
    }

    Platform.runLater(() -> {
      helperText.setText("");
      for (int x = 0; x < 4; x++) {
        for (int y = 0; y < 3; y++) {
          grid[x][y].getHoverPane().setVisible(false);

          if (grid[x][y].getClickHandler() != null) {
            grid[x][y].getTilePane()
                .removeEventHandler(MouseEvent.MOUSE_CLICKED, grid[x][y].getClickHandler());
            grid[x][y].setClickHandler(null);
          }
        }
      }
    });

    for (GUIPlayerTile playerTile : playerTiles.values()) {
      if (playerTile.getClickHandler() != null) {
        playerTile.getPlayerIcon()
            .removeEventHandler(MouseEvent.MOUSE_CLICKED, playerTile.getClickHandler());
        playerTile.setClickHandler(null);
      }
    }
  }

  /**
   * Highlights the selectable squares by reducing the opacity of the others
   *
   * @param squares a list of squares to be highlighted
   */
  public void highlightSelectableSquares(List<Target> squares) {
    Platform.runLater(() -> {
      for (int x = 0; x < 4; x++) {
        for (int y = 0; y < 3; y++) {
          grid[x][y].getHoverPane().setVisible(!containsTarget(squares, x, y));
        }
      }
    });
  }

  /**
   * Makes the given squares clickable in the grid
   *
   * @param squares target squares
   */
  public void enableSquareSelection(TargetType selectType, List<Target> squares, final boolean move,
      boolean skippable) {
    if (selectType == TargetType.ATTACK_ROOM) {
      Platform.runLater(() -> helperText.setText("Seleziona una stanza"));
    } else {
      Platform.runLater(() -> helperText.setText("Seleziona un quadrato"));
    }
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        if (containsTarget(squares, x, y)) {
          final int finalX = x;
          final int finalY = y;
          EventHandler<MouseEvent> clickHandler = event -> {
            try {
              if (move) {
                ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                    new SquareMoveSelectionEvent(AppGUI.getClient().getPlayerColor(), finalX,
                        finalY));
              } else {
                ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                    new SelectSquareEvent(AppGUI.getClient().getPlayerColor(), finalX, finalY));
              }
            } catch (RemoteException e) {
              Log.exception(e);
            }
            disableTargetSelection();
          };

          if (skippable) {
            skipEventHandler = event -> {
              try {
                ((BoardView) AppGUI.getClient().getBoardView())
                    .sendEvent(new SkipSelectionEvent(AppGUI.getClient().getPlayerColor()));
              } catch (RemoteException e) {
                Log.exception(e);
              }
              disableTargetSelection();
            };
            skipTurnActionButton.setOnAction(skipEventHandler);

            showSkip();
          }
          grid[x][y].setClickHandler(clickHandler);
          grid[x][y].getTilePane().addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
        }
      }
    }
  }

  /**
   * Makes the given players clickable in the grid
   *
   * @param players target players
   */
  public void enablePlayerSelection(List<Target> players, boolean skippable) {
    Platform.runLater(() -> helperText.setText("Seleziona un bersaglio"));
    for (Target target : players) {
      if (target.isPlayer() && playerTiles.containsKey(((Player) target).getColor())) {
        EventHandler<MouseEvent> clickHandler = event -> {
          try {
            ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(
                new SelectPlayerEvent(AppGUI.getClient().getPlayerColor(),
                    ((Player) target).getColor()));
          } catch (RemoteException e) {
            Log.exception(e);
          }
          disableTargetSelection();
        };

        if (skippable) {
          skipEventHandler = event -> {
            try {
              ((BoardView) AppGUI.getClient().getBoardView())
                  .sendEvent(new SkipSelectionEvent(AppGUI.getClient().getPlayerColor()));
            } catch (RemoteException e) {
              Log.exception(e);
            }
            disableTargetSelection();
          };
          skipTurnActionButton.setOnAction(skipEventHandler);

          showSkip();
        }

        playerTiles.get(((Player) target).getColor()).getPlayerIcon()
            .addEventHandler(MouseEvent.MOUSE_CLICKED, clickHandler);
        playerTiles.get(((Player) target).getColor()).setClickHandler(clickHandler);
      }
    }
  }

  /**
   * Checks if exists a target with the specified coordinates in the given list
   *
   * @param targets the checked list
   * @param x coordinate
   * @param y coordinate
   * @return true if exists a target with the given coordinates
   */
  private boolean containsTarget(List<Target> targets, int x, int y) {
    for (Target target : targets) {
      if (target.getSquare().getPosX() == x && target.getSquare().getPosY() == y) {
        return true;
      }
    }
    return false;
  }

  public void updateBlueWeapons(List<Weapon> weapons) {
    updateWeapons(blueWeapons, blueWeaponsHover, weapons, false);
  }

  public void updateRedWeapons(List<Weapon> weapons) {
    updateWeapons(redWeapons, redWeaponsHover, weapons, true);
  }

  public void updateYellowWeapons(List<Weapon> weapons) {
    updateWeapons(yellowWeapons, yellowWeaponsHover, weapons, true);
  }

  private void updateWeapons(Pane box, Pane boxHover, List<Weapon> weapons, boolean rotatedImages) {
    Platform.runLater(() -> {
      ColorAdjust bnEffect = new ColorAdjust();
      bnEffect.setSaturation(-1);

      for (Node weaponImageView : boxHover.getChildren()) {
        squareWeapons.remove(((Weapon) weaponImageView.getProperties().get("weapon")).getName());
        squareWeaponsHover
            .remove(((Weapon) weaponImageView.getProperties().get("weapon")).getName());
      }

      box.getChildren().clear();
      boxHover.getChildren().clear();

      for (Weapon weapon : weapons) {
        Image image;
        if (rotatedImages) {
          image = new Image("gui/assets/img/weapon/rotated/weapon_" + weapon.getSlug() + ".png");
        } else {
          image = new Image("gui/assets/img/weapon/weapon_" + weapon.getSlug() + ".png");
        }

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        if (rotatedImages) {
          imageView.setFitHeight(70);
        } else {
          imageView.setFitWidth(70);
        }
        imageView.setEffect(bnEffect);
        box.getChildren().add(imageView);

        ImageView imageViewHover = new ImageView(image);
        imageViewHover.setPreserveRatio(true);
        if (rotatedImages) {
          imageViewHover.setFitHeight(70);
        } else {
          imageViewHover.setFitWidth(70);
        }
        imageViewHover.setOpacity(0);
        imageViewHover.getProperties().put("weapon", weapon);
        boxHover.getChildren().add(imageViewHover);
        squareWeapons.put(weapon.getName(), imageView);
        squareWeaponsHover.put(weapon.getName(), imageViewHover);

        imageViewHover.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
          imageViewHover.setOpacity(1);
          imageViewHover.setScaleX(2);
          imageViewHover.setScaleY(2);
          if (rotatedImages) {
            imageViewHover.setTranslateX(50);
          } else {
            imageViewHover.setTranslateY(50);
          }
        });

        imageViewHover.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
          imageViewHover.setScaleX(1);
          imageViewHover.setScaleY(1);
          imageViewHover.setTranslateX(0);
          imageViewHover.setTranslateY(0);
          imageViewHover.setOpacity(0);
        });
      }
    });
  }

  public void showBoardWeaponsDeck(boolean full) {
    Platform.runLater(() -> boardHasWeapons.setVisible(full));
  }

  public void enableBoardWeapons(List<Weapon> weapons) {
    Platform.runLater(() -> {
      for (Weapon weapon : weapons) {
        if (squareWeapons.containsKey(weapon.getName())) {
          squareWeapons.get(weapon.getName()).setEffect(null);
          squareWeaponsHover.get(weapon.getName())
              .addEventHandler(MouseEvent.MOUSE_CLICKED, buyWeaponEventHandler);
        }
      }
    });
  }

  public void disableBoardWeapons() {
    Platform.runLater(() -> {
      ColorAdjust bnEffect = new ColorAdjust();
      bnEffect.setSaturation(-1);

      for (Entry<String, ImageView> node : squareWeaponsHover.entrySet()) {
        if (node.getValue().getEffect() != null) {
          node.getValue().removeEventHandler(MouseEvent.MOUSE_CLICKED, buyWeaponEventHandler);
          squareWeapons.get(node.getKey()).setEffect(bnEffect);
        }
      }
    });
  }

  public void updateKilltrack(List<Kill> kills, int total) {
    Platform.runLater(() -> {
      boardSkulls.getChildren().clear();
      for (Kill kill : kills) {
        Circle skull = new Circle();
        skull.setFill(Color.web(kill.getPlayerColor().getHexColor()));
        skull.setStrokeWidth(1);
        skull.setStroke(Color.BLACK);
        if (domination) {
          skull.setRadius(5);
        } else {
          skull.setRadius(9.5);
        }
        boardSkulls.getChildren().add(skull);
      }

      for (int i = 0; i < total - kills.size(); i++) {
        Circle skull = new Circle();
        skull.getStyleClass().add("boardSkull");
        if (domination) {
          skull.setRadius(5);
        } else {
          skull.setRadius(9.5);
        }
        boardSkulls.getChildren().add(skull);
      }
    });
  }

  public void updateSpawnpointDamages(AmmoColor spawnPointColor, List<PlayerColor> players) {
    HBox track = null;
    switch (spawnPointColor) {
      case BLUE:
        track = blueSpawnPointDamages;
        break;
      case RED:
        track = redSpawnPointDamages;
        break;
      case YELLOW:
        track = yellowSpawnPointDamages;
        break;
      case ANY:
        return;
    }

    if (track == null) {
      return;
    }
    track.getChildren().clear();

    for (PlayerColor kill : players) {
      Circle circle = new Circle();
      circle.setFill(Color.web(kill.getHexColor()));
      circle.setStrokeWidth(1);
      circle.setStroke(Color.BLACK);
      circle.setRadius(5);
      track.getChildren().add(circle);
    }
  }

  public void switchDashboardToFrenzy(PlayerColor color) {
    dashboardControllers.get(color).setFrenzy();
  }

  public void reset() {
    clearTurnActions();
    hideSkip();
    disableTargetSelection();
    disableBoardWeapons();
  }
}
