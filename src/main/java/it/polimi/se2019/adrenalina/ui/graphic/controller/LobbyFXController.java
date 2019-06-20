package it.polimi.se2019.adrenalina.ui.graphic.controller;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Player;
import java.awt.Font;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
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
  private Circle skullsImage;
  @FXML
  private ListView playerList;

  private final ObservableList<ListPlayer> players = FXCollections.observableArrayList();

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
    if (masterPlayer) {
      FXUtils.transition(lobbyConnecting, lobbyConfigurationMap);
    } else {
      FXUtils.transition(lobbyConnecting, lobbyPlayers);
    }
  }

  public void nextMap(ActionEvent actionEvent) {
    FXUtils.transition(lobbyConfigurationMap, lobbyConfigurationSkulls);
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
    } else if (diff == 1){
      lobbyPlayersSubtitle.setText(
          "Per iniziare la partita serve un altro giocatore");
    } else {
      lobbyPlayersSubtitle.setText(
          "Per iniziare la partita servono altri " + (Configuration.getInstance().getMinNumPlayers()
              - players.size()) + " giocatori");
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
        Circle masterIndicator = new Circle(5, Color.web("#ce1f08"));
        Label nameLabel = new Label(player.getName());
        nameLabel.setPadding(new Insets(0, 0, 0, 15));
        if (!player.isMaster()) {
          masterIndicator.setOpacity(0);
        }

        hBox.getChildren().add(masterIndicator);
        hBox.getChildren().add(nameLabel);
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
