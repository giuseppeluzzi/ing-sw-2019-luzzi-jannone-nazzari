package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.App;
import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.ClientConfig;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.ui.graphic.GUIBoardView;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogChangePlayerColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LobbyFXController {

  @FXML
  private BorderPane lobbyConnecting;
  @FXML
  private BorderPane lobbyConfigurationMap;
  @FXML
  private BorderPane lobbyConfigurationSkulls;
  @FXML
  private BorderPane lobbyPlayers;

  @FXML
  private Text lobbyPlayersSubtitle;
  @FXML
  private Circle map1Image;
  @FXML
  private Circle map2Image;
  @FXML
  private Circle map3Image;
  @FXML
  private Circle map4Image;
  @FXML
  private ToggleGroup map;
  @FXML
  private Circle skullsImage;
  @FXML
  private ListView playerList;

  @FXML
  private ImageView selectedMapImage;
  @FXML
  private Text selectedMapText;
  @FXML
  private Text gameModeText;
  @FXML
  private Text skullsText;
  @FXML
  private Text characterText;
  @FXML
  private Button buttonNextSkulls;

  private int mapId;

  private final ObservableList<ListPlayer> players = FXCollections.observableArrayList();
  private DialogChangePlayerColor changePlayerColorDialog;

  public void initialize() {
    lobbyConnecting.setVisible(true);
    lobbyConfigurationMap.setVisible(false);
    lobbyConfigurationSkulls.setVisible(false);
    lobbyPlayers.setVisible(false);

    map1Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map1.png"), -110, -100, 233, 175, false));
    map2Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map2.png"), -110, -100, 233, 175, false));
    map3Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map3.png"), -120, -100, 233, 175, false));
    map4Image.setFill(
        new ImagePattern(new Image("gui/assets/img/map4.png"), -110, -100, 233, 175, false));
    skullsImage.setFill(
        new ImagePattern(new Image("gui/assets/img/skulls.png"), -100, -100, 200, 200, false));

    playerList.setItems(players);
    playerList.setCellFactory(param -> new PlayerListRow());
  }

  public void endLoading(boolean masterPlayer) {
    Platform.runLater(() -> {
      try {
        gameModeText
            .setText(
                "Modalità " + (AppGUI.getClient().isDomination() ? "Dominazione" : "Classica"));
      } catch (RemoteException e) {
        Log.exception(e);
      }

      if (masterPlayer) {
        FXUtils.lobbyTransition(lobbyConnecting, lobbyConfigurationMap);
      } else {
        FXUtils.lobbyTransition(lobbyConnecting, lobbyPlayers);
      }
    });

    IntegerProperty seconds;
    try {
      seconds = ((GUIBoardView) AppGUI.getClient().getBoardView())
          .getTimer().getSeconds();
    } catch (RemoteException e) {
      return;
    }
    seconds.addListener(change -> {
      Platform.runLater(() -> lobbyPlayersSubtitle.setText(
          "La partita inizierà tra " + seconds.getValue() + " " + (seconds.getValue() == 1
              ? "secondo" : "secondi")));
    });
  }

  public void nextMap(ActionEvent actionEvent) {
    FXUtils.lobbyTransition(lobbyConfigurationMap, lobbyConfigurationSkulls);
    buttonNextSkulls.requestFocus();
    int mapId = Integer.valueOf(((Styleable) map.getSelectedToggle()).getId().replace("map", ""));
    try {
      ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(new MapSelectionEvent(mapId));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public void nextSkulls(ActionEvent actionEvent) {
    FXUtils.lobbyTransition(lobbyConfigurationSkulls, lobbyPlayers);
  }

  public void addPlayer(Player player) {
    players.add(new ListPlayer(player.getName(), player.getColor(), player.isMaster()));
    updateTitle();
  }

  public void setPlayerMaster(PlayerColor playerColor) {
    for (ListPlayer player : players) {
      if (player.getColor() == playerColor) {
        player.setMaster(true);
      } else {
        player.setMaster(false);
      }
    }
    playerList.refresh();
  }

  public void setPlayerColor(PlayerColor playerColor, PlayerColor newColor) {
    if (playerColor != null) {
      for (ListPlayer player : players) {
        if (player.getColor() == playerColor) {
          player.setColor(newColor);
          playerList.refresh();
          break;
        }
      }
    }
  }

  public void removePlayer(PlayerColor playerColor) {
    for (ListPlayer player : players) {
      if (player.getColor() == playerColor) {
        players.remove(player);
        updateTitle();
        return;
      }
    }
  }

  private void updateTitle() {
    int diff = ClientConfig.getInstance().getMinNumPlayers() - players.size();
    if (diff <= 0) {
      lobbyPlayersSubtitle.setText("La partita inizierà a breve");
    } else if (diff == 1) {
      lobbyPlayersSubtitle.setText(
          "Per iniziare la partita serve un altro giocatore");
    } else {
      lobbyPlayersSubtitle.setText(
          "Per iniziare la partita servono altri " + (ClientConfig.getInstance().getMinNumPlayers()
              - players.size()) + " giocatori");
    }
  }

  public void setMapId(int map) {
    mapId = map;
    Platform.runLater(() -> {
      selectedMapImage.setImage(new Image("gui/assets/img/map" + map + ".png"));
      selectedMapText.setText("Mappa " + map);
    });
  }

  public int getMapId() {
    return mapId;
  }

  public void setSkulls(int skulls) {
    Platform.runLater(() -> skullsText.setText("Teschi: " + skulls));
  }

  public void closeChangeColorDialog() {
    if (changePlayerColorDialog != null) {
      Platform.runLater(() -> changePlayerColorDialog.close());
    }
  }

  private static class ListPlayer {

    private final String name;
    private PlayerColor color;
    private boolean master;

    public ListPlayer(String name, PlayerColor color, boolean master) {
      this.name = name;
      this.color = color;
      this.master = master;
    }

    public String getName() {
      return name;
    }

    public PlayerColor getColor() {
      return color;
    }

    public void setColor(PlayerColor color) {
      this.color = color;
    }

    public boolean isMaster() {
      return master;
    }

    public void setMaster(boolean master) {
      this.master = master;
    }
  }

  private static class PlayerListRow extends ListCell<ListPlayer> {

    @Override
    public void updateItem(ListPlayer player, boolean empty) {
      super.updateItem(player, empty);

      if (player != null) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15));
        hBox.setAlignment(Pos.CENTER_LEFT);
        Circle masterIndicator = new Circle(5, Color.web("#393939"));
        Label nameLabel = new Label(player.getName());
        nameLabel.setFont(new Font(18));
        nameLabel.setStyle("-fx-font-weight: bold;");
        nameLabel.setTextFill(Color.web(player.getColor().getHexColor()));
        nameLabel.setPadding(new Insets(0, 15, 0, 15));
        if (!player.isMaster()) {
          masterIndicator.setOpacity(0);
        }

        hBox.getChildren().add(masterIndicator);
        hBox.getChildren().add(nameLabel);

        try {
          if (player.getName().equals(AppGUI.getClient().getName())) {
            ImageView editIcon = new ImageView("gui/assets/img/edit_icon.png");
            editIcon.setFitWidth(16);
            editIcon.setFitHeight(16);
            editIcon.setMouseTransparent(false);
            editIcon.setCursor(Cursor.HAND);
            editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
              AppGUI.getLobbyFXController().changePlayerColorDialog = new DialogChangePlayerColor();
              AppGUI.getLobbyFXController().changePlayerColorDialog.show();
              event.consume();
            });
            hBox.getChildren().add(editIcon);
          }
        } catch (RemoteException e) {
          Log.exception(e);
        }

        Platform.runLater(() -> setGraphic(hBox));
      } else {
        Platform.runLater(() -> {
          HBox hBox = new HBox();
          hBox.setPadding(new Insets(15));
          setGraphic(hBox);
        });
      }
    }
  }
}
