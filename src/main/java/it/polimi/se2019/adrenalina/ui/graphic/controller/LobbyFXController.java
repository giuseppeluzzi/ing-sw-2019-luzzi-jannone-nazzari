package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.rmi.RemoteException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

  private final ObservableList<ListPlayer> players = FXCollections.observableArrayList();

  public void initialize() {
    lobbyConnecting.setVisible(true);
    lobbyConfigurationMap.setVisible(false);
    lobbyConfigurationSkulls.setVisible(false);
    lobbyPlayers.setVisible(true);

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
        FXUtils.transition(lobbyConnecting, lobbyConfigurationMap);
      } else {
        FXUtils.transition(lobbyConnecting, lobbyPlayers);
      }
    });
  }

  public void nextMap(ActionEvent actionEvent) {
    FXUtils.transition(lobbyConfigurationMap, lobbyConfigurationSkulls);
    int mapId = Integer.valueOf(((Styleable) map.getSelectedToggle()).getId().replace("map", ""));
    try {
      ((BoardView) AppGUI.getClient().getBoardView()).sendEvent(new MapSelectionEvent(mapId));
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  public void nextSkulls(ActionEvent actionEvent) {
    FXUtils.transition(lobbyConfigurationSkulls, lobbyPlayers);
  }

  public void addPlayer(Player player) {
    players.add(new ListPlayer(player.getName(), player.getColor(), player.isMaster()));
    updateTitle();
  }

  public void setPlayerMaster(PlayerColor playerColor) {
    for (ListPlayer player : players) {
      if (player.getColor() == playerColor) {
        Log.debug("set " + player.getName() + " master");
        player.setMaster(true);
      } else {
        player.setMaster(false);
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
    int diff = Configuration.getInstance().getMinNumPlayers() - players.size();
    if (diff <= 0) {
      lobbyPlayersSubtitle.setText("La partita inizierà a breve");
    } else if (diff == 1) {
      lobbyPlayersSubtitle.setText(
          "Per iniziare la partita serve un altro giocatore");
    } else {
      lobbyPlayersSubtitle.setText(
          "Per iniziare la partita servono altri " + (Configuration.getInstance().getMinNumPlayers()
              - players.size()) + " giocatori");
    }
  }

  public void setMap(int map) {
    Platform.runLater(() -> {
      selectedMapImage.setImage(new Image("gui/assets/img/map" + map + ".png"));
      selectedMapText.setText("Mappa " + map);
    });
  }

  public void setSkulls(int skulls) {
    Platform.runLater(() -> skullsText.setText("Teschi: " + skulls));
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
          if (player.color == AppGUI.getClient().getPlayerColor()) {
            ImageView editIcon = new ImageView("gui/assets/img/edit_icon.png");
            editIcon.setFitWidth(16);
            editIcon.setFitHeight(16);
            hBox.getChildren().add(editIcon);
          }
        } catch (RemoteException e) {
          Log.exception(e);
        }

        Platform.runLater(() -> {
          setGraphic(hBox);
        });
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
