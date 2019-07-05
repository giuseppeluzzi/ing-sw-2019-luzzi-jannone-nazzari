package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.ClientConfig;
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
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.ui.graphic.GUITimer;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.Dialog;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class BoardFXController {

  private static final String WEAPON_PROP = "weapon";
  private static final String TARGET_PROP = "target";
  private static final String DASHBOARD_COLOR_PROP = "color";
  private static final String TILE_PLAYER_COLOR_PROP = "playerColor";
  private static final double LOG_PAST_OPACITY = 0.6;

  @FXML
  private ProgressBar turnProgressBar;

  private Timeline taskTurnProgressBar;

  @FXML
  private GridPane boardGrid;

  @FXML
  private GridPane mapGrid;
  @FXML
  private GridPane bottomGrid;
  @FXML
  private Pane playerDashboardContainer;
  @FXML
  private VBox enemyDashboards;
  @FXML
  private Button skipTurnActionButton;
  @FXML
  private Button noPowerUpTurnActionButton;
  @FXML
  private FlowPane turnActionButtons;

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
  @FXML
  private VBox gameLog;

  private Pane boardSkulls;

  private final GUITimer turnTimer;
  private boolean domination;
  private GUIGridSquare[][] grid;

  private final EnumMap<PlayerColor, GUIPlayerTile> playerTiles;

  private final EnumMap<PlayerColor, DashboardFXController> dashboardControllers;
  private final HashMap<String, ImageView> squareWeapons;
  private final HashMap<String, ImageView> squareWeaponsHover;

  private final EventHandler<MouseEvent> selectTargetEventHandler;
  private final EventHandler<MouseEvent> buyWeaponEventHandler;

  private final ObservableList<String> gameLogMessages;

  public BoardFXController() {
    turnTimer = new GUITimer();
    gameLogMessages = FXCollections.observableArrayList("", "", "", "", "");
    dashboardControllers = new EnumMap<>(PlayerColor.class);
    playerTiles = new EnumMap<>(PlayerColor.class);
    squareWeapons = new HashMap<>();
    squareWeaponsHover = new HashMap<>();

    selectTargetEventHandler = this::handleSelectTarget;
    buyWeaponEventHandler = this::handleWeaponBuy;
  }

  public void initialize() {
    grid = new GUIGridSquare[4][3];

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        TilePane cellTilePane = new TilePane();
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

    mapGrid.setStyle(
        "-fx-background-image: url(\"gui/assets/img/map1.png\");");

    //noinspection RedundantCast
    gameLogMessages.addListener((ListChangeListener<String>) this::onLogUpdate);

    noPowerUpTurnActionButton.setOnAction(this::handleNoPowerUpUsage);
    skipTurnActionButton.setOnAction(this::handleSkipSelection);
  }

  public void setMapId(int mapId) {
    Platform.runLater(() ->
        mapGrid.setStyle(
            "-fx-background-image: url(\"gui/assets/img/map" + mapId + ".png\");")
    );
  }

  public void setHelpText(String text) {
    if (text.isEmpty()) {
      gameLog.getChildren().get(gameLog.getChildren().size() - 1).setOpacity(LOG_PAST_OPACITY);
    } else {
      gameLogMessages.add(text);
    }
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

  public void setCurrentEnabledDashboard(PlayerColor enabledColor) {
    for (Entry<PlayerColor, DashboardFXController> dashboard : dashboardControllers.entrySet()) {
      if (dashboard.getKey() == enabledColor) {
        dashboard.getValue().enable();
      } else {
        dashboard.getValue().disable();
      }
    }
  }

  public void loadPlayerDashboard(PlayerColor color) {
    FXMLLoader loaderPlayerDashboard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/PlayerDashboard.fxml"));
    PlayerDashboardFXController playerDashboardFXController = new PlayerDashboardFXController(this,
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

    Platform.runLater(() -> {
      try {
        Parent playerDashboard = loaderPlayerDashboard.load();
        playerDashboard.getProperties().put(DASHBOARD_COLOR_PROP, color);
        GridPane.setRowIndex(playerDashboard, 0);
        GridPane.setColumnIndex(playerDashboard, 1);
        GridPane.setRowSpan(playerDashboard, 2);
        bottomGrid.getChildren().add(playerDashboard);

        Player player = AppGUI.getClient().getBoardView().getBoard().getPlayerByColor(color);
        dashboardControllers.get(color).getDashboardNameLabel()
            .setText(player.getName() + " (" + player.getScore() + ")");
        dashboardControllers.get(color).getDashboardNameLabel()
            .setFill(Color.web(color.getHexColor()));
      } catch (IOException e) {
        Log.exception(e);
      } catch (InvalidPlayerException ignored) {
        //
      }
    });
  }

  public void loadBoardSkulls(int skulls) {
    Platform.runLater(() -> {
      boardSkulls.getChildren().clear();
      double radius;
      if (domination) {
        radius = 5;
      } else {
        radius = 9.5;
      }

      for (int i = 0; i < skulls; i++) {
        Circle skull = new Circle();
        skull.getStyleClass().add("boardSkull");
        skull.setRadius(radius);
        boardSkulls.getChildren().add(skull);

      }
      boardSkulls.setVisible(true);
    });
  }

  private void loadDominationInterface() {
    normalBoardSkulls.setVisible(false);
    dominationKilltrack.setVisible(true);
    boardSkulls = dominationBoardSkulls;
  }

  public void loadEnemyDashboard(PlayerColor color) {
    FXMLLoader loaderEnemyDashboard = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/EnemyDashboard.fxml"));

    DashboardFXController enemyDashboardFXController = new EnemyDashboardFXController(this, color);
    loaderEnemyDashboard.setController(enemyDashboardFXController);
    dashboardControllers.put(color, enemyDashboardFXController);

    Platform.runLater(() -> {
      try {
        Parent dashboard = loaderEnemyDashboard.load();
        dashboard.getProperties().put(DASHBOARD_COLOR_PROP, color);
        enemyDashboards.getChildren().add(dashboard);

        Player player = AppGUI.getClient().getBoardView().getBoard().getPlayerByColor(color);
        dashboardControllers.get(color).getDashboardNameLabel()
            .setText(player.getName() + " (" + player.getScore() + ")");
        dashboardControllers.get(color).getDashboardNameLabel()
            .setFill(Color.web(color.getHexColor()));
      } catch (IOException e) {
        Log.exception(e);
      } catch (InvalidPlayerException ignored) {
        //
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
          if (color == child.getProperties().get(DASHBOARD_COLOR_PROP)) {
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
    if (playerTiles.containsKey(playerColor)) {
      final Parent stackPane = playerTiles.get(playerColor).getPlayerIcon().getParent();
      grid[playerTiles.get(playerColor).getX()][playerTiles.get(playerColor).getY()].getTilePane()
          .getChildren().remove(stackPane);
      playerTiles.remove(playerColor);
    }

    Circle playerIcon = new Circle(13, Color.web(playerColor.getHexColor()));
    playerIcon.getStyleClass().add("player");
    playerIcon.getProperties().put(TILE_PLAYER_COLOR_PROP, playerColor);
    playerIcon.setStroke(Color.WHITE);
    playerIcon.setStrokeWidth(1);

    Circle playerIconHover = new Circle(13);
    playerIconHover.getStyleClass().add("disabledSquare");
    playerIconHover.setStroke(Color.BLACK);
    playerIconHover.setStrokeWidth(1);
    playerIconHover.setVisible(false);

    grid[posX][posY].getTilePane().getChildren().add(0, new StackPane(playerIcon, playerIconHover));

    if (playerTiles.containsKey(playerColor)) {
      playerTiles.get(playerColor).setPlayerIcon(playerIcon);
      playerTiles.get(playerColor).setPlayerIconHover(playerIconHover);
    } else {
      final GUIPlayerTile playerTile = new GUIPlayerTile(posX, posY, playerColor, playerIcon,
          playerIconHover);
      playerTiles.put(playerColor, playerTile);
    }
  }

  public void showTurnActions(List<TurnAction> turnActions) {
    Platform.runLater(() -> {
      turnActionButtons.getChildren().clear();
      for (TurnAction turnAction : turnActions) {
        Button button = new Button(turnAction.getName());
        button.setTooltip(new Tooltip(turnAction.getDescription()));

        button.setOnAction(event -> {
          stopTurnTimer();
          try {
            AppGUI.getClient().getBoardView().sendEvent(
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
    Platform.runLater(() -> {
      for (Node node : new ArrayList<>(turnActionButtons.getChildren())) {
        if (node.getId() == null) {
          turnActionButtons.getChildren().remove(node);
        }
      }
    });
  }

  public void showSkip() {
    Platform.runLater(() -> {
      skipTurnActionButton.setManaged(true);
      skipTurnActionButton.setVisible(true);
    });
  }

  public void hideSkip() {
    Platform.runLater(() -> {
      skipTurnActionButton.setVisible(false);
      skipTurnActionButton.setManaged(false);
    });
  }

  public void showPowerUpSkip() {
    Platform.runLater(() -> {
      noPowerUpTurnActionButton.setManaged(true);
      noPowerUpTurnActionButton.setVisible(true);
    });
  }

  public void hidePowerUpSkip() {
    Platform.runLater(() -> {
      noPowerUpTurnActionButton.setVisible(false);
      noPowerUpTurnActionButton.setManaged(false);
    });
  }

  /**
   * Removes the selectable view from the map grid
   */
  public void disableTargetSelection() {
    hideSkip();

    Platform.runLater(() -> {
      setHelpText("");

      List<GUITile> guiTiles = new ArrayList<>(getGridTiles());
      guiTiles.addAll(playerTiles.values());

      for (GUITile tile : guiTiles) {
        tile.enableTile();
        tile.getTilePane().getProperties().remove("move");
        tile.getTilePane().removeEventHandler(MouseEvent.MOUSE_CLICKED, selectTargetEventHandler);
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
      Platform.runLater(() -> setHelpText("Seleziona una stanza"));
    } else {
      Platform.runLater(() -> setHelpText("Seleziona un quadrato"));
    }

    List<GUITile> tiles = new ArrayList<>(getGridTiles());

    if (skippable) {
      showSkip();
    }

    for (GUITile tile : tiles) {
      boolean enabled = false;
      for (Target square : squares) {
        if (tile.isTarget(square)) {
          tile.enableTile();
          enabled = true;

          tile.getTilePane().getProperties().put(TARGET_PROP, square);
          tile.getTilePane().addEventHandler(MouseEvent.MOUSE_CLICKED, selectTargetEventHandler);

          if (move) {
            tile.getTilePane().getProperties().put("move", true);
          }
          break;
        }
      }
      if (!enabled) {
        tile.disableTile();
      }
    }
  }

  /**
   * Makes the given players clickable in the grid
   *
   * @param targets target players
   */
  public void enableTargetSelection(List<Target> targets, boolean skippable) {
    Platform.runLater(() -> setHelpText("Seleziona un bersaglio"));
    List<GUITile> tiles = new ArrayList<>(getGridTiles());
    tiles.addAll(playerTiles.values());

    for (GUITile tile : tiles) {
      boolean enabled = false;

      for (Target target : targets) {
        if (tile.isTarget(target)) {
          tile.enableTile();
          enabled = true;
          tile.getTilePane().getProperties().put(TARGET_PROP, target);
          tile.getTilePane().addEventHandler(MouseEvent.MOUSE_CLICKED, selectTargetEventHandler);
          break;
        }
      }
      if (!enabled) {
        tile.disableTile();
      }
    }

    if (skippable) {
      showSkip();
    }
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
        squareWeapons.remove(((Weapon) weaponImageView.getProperties().get(WEAPON_PROP)).getName());
        squareWeaponsHover
            .remove(((Weapon) weaponImageView.getProperties().get(WEAPON_PROP)).getName());
      }

      box.getChildren().clear();
      boxHover.getChildren().clear();

      for (Weapon weapon : weapons) {
        Image image;
        ImageView imageView = new ImageView();
        ImageView imageViewHover = new ImageView();

        imageView.setPreserveRatio(true);
        imageView.setEffect(bnEffect);

        imageViewHover.setPreserveRatio(true);
        imageViewHover.setOpacity(0);
        imageViewHover.getProperties().put(WEAPON_PROP, weapon);

        if (rotatedImages) {
          imageView.setFitHeight(70);
          imageViewHover.setFitHeight(70);
          imageViewHover.getProperties().put("rotated", true);
          image = new Image("gui/assets/img/weapon/rotated/weapon_" + weapon.getSlug() + ".png");
        } else {
          imageView.setFitWidth(70);
          imageViewHover.setFitWidth(70);
          image = new Image("gui/assets/img/weapon/weapon_" + weapon.getSlug() + ".png");
        }

        imageView.setImage(image);
        imageViewHover.setImage(image);

        box.getChildren().add(imageView);
        boxHover.getChildren().add(imageViewHover);

        squareWeapons.put(weapon.getName(), imageView);
        squareWeaponsHover.put(weapon.getName(), imageViewHover);

        imageViewHover
            .addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET,
                BoardFXController::handleBoardWeaponHoverIn);
        imageViewHover
            .addEventHandler(MouseEvent.MOUSE_EXITED_TARGET,
                BoardFXController::handleBoardWeaponHoverOut);
      }
    });
  }

  public void showBoardWeaponsDeck(boolean full) {
    Platform.runLater(() -> boardHasWeapons.setVisible(full));
  }

  public void enableBoardWeapons(List<Weapon> weapons) {
    Platform.runLater(() -> {
      setHelpText("Seleziona un'arma da raccogliere");
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
      boardSkulls.setVisible(true);
      boardSkulls.getChildren().clear();

      double radius;
      if (domination) {
        radius = 5;
      } else {
        radius = 9.5;
      }

      for (Kill kill : kills) {
        Circle skull = new Circle();
        skull.setFill(Color.web(kill.getPlayerColor().getHexColor()));
        skull.setStrokeWidth(1);
        skull.setStroke(Color.BLACK);
        skull.setRadius(radius);

        if (kill.isOverKill()) {
          Circle doubleSkull = new Circle();
          doubleSkull.setFill(Color.web(kill.getPlayerColor().getHexColor()));
          doubleSkull.setStrokeWidth(1);
          doubleSkull.setStroke(Color.BLACK);
          doubleSkull.setRadius(radius);

          VBox vBox = new VBox();
          vBox.setPadding(new Insets(-5, 0, 0, 0));
          vBox.setAlignment(Pos.CENTER);
          vBox.getChildren().add(doubleSkull);
          vBox.getChildren().add(skull);
          vBox.setTranslateY(2);

          vBox.setSpacing(-radius / 2 * 3);
          boardSkulls.getChildren().add(vBox);
        } else {
          boardSkulls.getChildren().add(skull);
        }
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
    HBox finalTrack = track;

    Platform.runLater(() -> {
      finalTrack.getChildren().clear();

      for (PlayerColor kill : players) {
        Circle circle = new Circle();
        circle.setFill(Color.web(kill.getHexColor()));
        circle.setStrokeWidth(1);
        circle.setStroke(Color.BLACK);
        circle.setRadius(5);
        finalTrack.getChildren().add(circle);
      }
    });
  }

  public void switchDashboardToFrenzy(PlayerColor color) {
    dashboardControllers.get(color).setFrenzy();
  }

  public void reset() {
    Platform.runLater(() -> setHelpText(""));

    clearTurnActions();
    hideSkip();
    hidePowerUpSkip();
    disableTargetSelection();
    disableBoardWeapons();
  }

  public List<GUIGridSquare> getGridTiles() {
    List<GUIGridSquare> tiles = new ArrayList<>();
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 3; y++) {
        tiles.add(grid[x][y]);
      }
    }
    return tiles;
  }


  private Color getTextColorFromAnsi(String textSpan) {
    String colorString = "\u001b" + textSpan.substring(0, textSpan.indexOf('m') + 1);
    for (ANSIColor color : ANSIColor.values()) {
      if (color.toString(true).equals(colorString) || color.toString(false)
          .equals(colorString)) {
        return Color.web(color.getHexColor());
      }
    }
    return null;
  }

  private void handleWeaponBuy(MouseEvent event) {
    final String weaponName = ((Weapon) ((Node) event.getSource()).getProperties()
        .get(WEAPON_PROP))
        .getName();

    stopTurnTimer();
    disableBoardWeapons();

    try {
      AppGUI.getClient().getBoardView().sendEvent(
          new PlayerCollectWeaponEvent(AppGUI.getClient().getPlayerColor(), weaponName));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  void handleSelectTarget(MouseEvent event) {
    event.consume();

    final Target target = (Target) ((Node) event.getSource()).getProperties().get(TARGET_PROP);

    stopTurnTimer();

    try {
      if (((Node) event.getSource()).getProperties().containsKey("move")) {
        AppGUI.getClient().getBoardView().sendEvent(
            new SquareMoveSelectionEvent(AppGUI.getClient().getPlayerColor(),
                ((Square) target).getPosX(), ((Square) target).getPosY()));
      } else {
        if (target.isPlayer()) {
          AppGUI.getClient().getBoardView().sendEvent(
              new SelectPlayerEvent(AppGUI.getClient().getPlayerColor(),
                  ((Player) target).getColor()));
        } else {
          AppGUI.getClient().getBoardView().sendEvent(
              new SelectSquareEvent(AppGUI.getClient().getPlayerColor(),
                  ((Square) target).getPosX(), ((Square) target).getPosY()));
        }
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }

    disableTargetSelection();
  }

  private void handleSkipSelection(ActionEvent event) {
    try {
      AppGUI.getClient().getBoardView().sendEvent(
          new SkipSelectionEvent(AppGUI.getClient().getPlayerColor()));
    } catch (RemoteException e) {
      Log.debug(event.getSource().toString());
      Log.exception(e);
    }

    disableTargetSelection();
  }

  private void onLogUpdate(Change<? extends String> change) {
    Platform.runLater(() -> {
      change.next();
      if (change.getList().size() > 5) {
        gameLog.getChildren().remove(0);
      }
      if (!gameLog.getChildren().isEmpty()) {
        gameLog.getChildren().get(gameLog.getChildren().size() - 1)
            .setOpacity(LOG_PAST_OPACITY);
      }

      String newLine = change.getAddedSubList().get(0);
      String[] splitLine = newLine.split("\\u001b");

      TextFlow textFlow = new TextFlow();
      textFlow.getStyleClass().add("logLine");

      for (String span : splitLine) {
        Text textSpan = new Text();
        textSpan.setFill(Color.WHITE);
        if (splitLine.length > 1) {
          if (span.indexOf('[') == -1) {
            textSpan.setText(span);
          } else {
            textSpan.setText(span.substring(span.indexOf('m') + 1));
            textSpan.setFill(getTextColorFromAnsi(span));
          }
        } else {
          textSpan.setText(newLine);
        }
        textFlow.getChildren().add(textSpan);
      }
      gameLog.getChildren().add(textFlow);
    });
  }

  private void handleNoPowerUpUsage(ActionEvent event) {
    try {
      AppGUI.getClient().getBoardView().sendEvent(
          new PlayerPowerUpEvent(AppGUI.getClient().getPlayerColor(), null, null));
      hidePowerUpSkip();
      AppGUI.getPlayerDashboardFXController().disablePowerUps();
    } catch (RemoteException e) {
      Log.debug(event.getSource().toString());
      Log.exception(e);
    }
  }

  private static void handleBoardWeaponHoverIn(MouseEvent event) {
    Node imageViewHover = (Node) event.getSource();
    imageViewHover.setOpacity(1);
    imageViewHover.setScaleX(2.5);
    imageViewHover.setScaleY(2.5);
    if (imageViewHover.getProperties().containsKey("rotated")) {
      imageViewHover.setTranslateX(80);
    } else {
      imageViewHover.setTranslateY(88);
    }
    imageViewHover.setEffect(null);
  }

  private static void handleBoardWeaponHoverOut(MouseEvent event) {
    ColorAdjust bnEffect = new ColorAdjust();
    bnEffect.setSaturation(-1);

    Node imageViewHover = (Node) event.getSource();
    imageViewHover.setScaleX(1);
    imageViewHover.setScaleY(1);
    imageViewHover.setTranslateX(0);
    imageViewHover.setTranslateY(0);
    imageViewHover.setOpacity(0);
    imageViewHover.setEffect(bnEffect);
  }

  static void handlePlayerWeaponHoverIn(MouseEvent event) {
    Node imageViewHover = (Node) event.getSource();
    imageViewHover.setTranslateX(-260);
  }

  static void handlePlayerWeaponHoverOut(MouseEvent event) {
    Node imageViewHover = (Node) event.getSource();
    imageViewHover.setTranslateX(0);
  }

  private void cancelInput() {
    Platform.runLater(() -> {
      turnProgressBar.setProgress(0);
      taskTurnProgressBar.stop();
      setHelpText("Tempo di attesa scaduto! Salti il turno!");
      reset();
    });
  }

  private void initializeProgressBar() {
    if (taskTurnProgressBar == null) {
      taskTurnProgressBar = new Timeline(
          new KeyFrame(
              Duration.ZERO,
              new KeyValue(turnProgressBar.progressProperty(), 1)
          ),
          new KeyFrame(
              Duration.seconds(ClientConfig.getInstance().getTurnTimeout()),
              new KeyValue(turnProgressBar.progressProperty(), 0)
          )
      );
    }
  }

  public void startTurnTimer(Dialog dialog) {
    initializeProgressBar();
    turnTimer.start(ClientConfig.getInstance().getTurnTimeout(), () -> {
      Platform.runLater(dialog::close);
      cancelInput();
    });
    taskTurnProgressBar.playFromStart();
  }

  public void startTurnTimer() {
    initializeProgressBar();
    turnTimer.start(ClientConfig.getInstance().getTurnTimeout(), this::cancelInput);
    taskTurnProgressBar.playFromStart();
  }

  public void stopTurnTimer() {
    turnTimer.stop();
    taskTurnProgressBar.stop();
    Platform.runLater(() -> turnProgressBar.setProgress(0));
  }
}
