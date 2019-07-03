package it.polimi.se2019.adrenalina.ui.graphic.controller;

import static it.polimi.se2019.adrenalina.ui.UIUtils.getFirstKillshotIndex;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Kill;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FinalRanksFXController {

  public static final String DARK_GREY = "#B0B0B0";
  public static final String LIGHT_GREY = "#E0E0E0";
  public static final String SUBTITLE_CLASS = "subtitle";

  @FXML
  private GridPane ranksTable;

  private StackPane stripedStackPane(Text element, int i, Pos alignment) {
    StackPane output = new StackPane(element);
    if (i % 2 == 1) {
      output.setBackground(new Background(
          new BackgroundFill(Color.web(LIGHT_GREY), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    output.setAlignment(alignment);
    return output;
  }

  public void initialize() {
    final Board board;
    try {
      board = AppGUI.getClient().getBoardView().getBoard();
    } catch (RemoteException e) {
      Log.exception(e);
      return;
    }

    List<Player> players = board.getPlayers();
    List<Kill> killShots = board.getKillShots();

    Collections.sort(players, (p1, p2) -> {
      if (p1.getScore() == p2.getScore()) {
        int p1Index = getFirstKillshotIndex(killShots, p1.getColor());
        int p2Index = getFirstKillshotIndex(killShots, p2.getColor());
        if (p1Index == p2Index) {
          return 0;
        }
        return p1Index < p2Index ? -1 : 1;
      }
      return p1.getScore() > p2.getScore() ? -1 : 1;
    });
    Text pos = new Text("#");
    pos.getStyleClass().add("text");
    pos.getStyleClass().add(SUBTITLE_CLASS);
    Text name = new Text("Giocatore");
    name.getStyleClass().add("text");
    name.getStyleClass().add(SUBTITLE_CLASS);
    Text scoreTitle = new Text("Punteggio");
    scoreTitle.getStyleClass().add("text");
    scoreTitle.getStyleClass().add(SUBTITLE_CLASS);
    Text killsTitle = new Text("Uccisioni");
    killsTitle.getStyleClass().add("text");
    killsTitle.getStyleClass().add(SUBTITLE_CLASS);
    Text overkillsTitle = new Text("Infierito");
    overkillsTitle.getStyleClass().add("text");
    overkillsTitle.getStyleClass().add(SUBTITLE_CLASS);

    StackPane posTitlePane = new StackPane(pos);
    posTitlePane.setBackground(
        new Background(new BackgroundFill(Color.web(DARK_GREY), CornerRadii.EMPTY, Insets.EMPTY)));
    StackPane nameTitlePane = new StackPane(name);
    nameTitlePane.setBackground(
        new Background(new BackgroundFill(Color.web(DARK_GREY), CornerRadii.EMPTY, Insets.EMPTY)));
    nameTitlePane.setAlignment(Pos.CENTER_LEFT);
    StackPane scoreTitlePane = new StackPane(scoreTitle);
    scoreTitlePane.setBackground(
        new Background(new BackgroundFill(Color.web(DARK_GREY), CornerRadii.EMPTY, Insets.EMPTY)));
    StackPane killsTitlePane = new StackPane(killsTitle);
    killsTitlePane.setBackground(
        new Background(new BackgroundFill(Color.web(DARK_GREY), CornerRadii.EMPTY, Insets.EMPTY)));
    StackPane overkillsTitlePane = new StackPane(overkillsTitle);
    overkillsTitlePane.setBackground(
        new Background(new BackgroundFill(Color.web(DARK_GREY), CornerRadii.EMPTY, Insets.EMPTY)));

    GridPane.setColumnIndex(posTitlePane, 0);
    GridPane.setRowIndex(posTitlePane, 0);
    GridPane.setColumnIndex(nameTitlePane, 1);
    GridPane.setRowIndex(nameTitlePane, 0);
    GridPane.setColumnIndex(scoreTitlePane, 2);
    GridPane.setRowIndex(scoreTitlePane, 0);
    GridPane.setColumnIndex(killsTitlePane, 3);
    GridPane.setRowIndex(killsTitlePane, 0);
    GridPane.setColumnIndex(overkillsTitlePane, 4);
    GridPane.setRowIndex(overkillsTitlePane, 0);

    ranksTable.getChildren().add(posTitlePane);
    ranksTable.getChildren().add(nameTitlePane);
    ranksTable.getChildren().add(scoreTitlePane);
    ranksTable.getChildren().add(killsTitlePane);
    ranksTable.getChildren().add(overkillsTitlePane);

    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);
      long killShotCount = killShots.stream().filter(x -> x.getPlayerColor() == player.getColor())
          .count();
      long overkillCount = killShots.stream()
          .filter(x -> x.getPlayerColor() == player.getColor() && x.isOverKill()).count();
      Text position = new Text(Integer.toString(i + 1));
      position.getStyleClass().add("text");
      Text playerName = new Text(
          String.format("%s (%s)", player.getName(), player.getColor().getCharacterName()));
      playerName.getStyleClass().add("text");
      playerName.setFill(Color.web(player.getColor().getHexColor()));
      Text score = new Text(Integer.toString(player.getScore()));
      score.getStyleClass().add("text");
      Text kills = new Text(Long.toString(killShotCount));
      kills.getStyleClass().add("text");
      Text overKills = new Text(Long.toString(overkillCount));
      overKills.getStyleClass().add("text");

      StackPane positionPane = stripedStackPane(position, i, Pos.CENTER);
      StackPane playerNamePane = stripedStackPane(playerName, i, Pos.CENTER_LEFT);
      StackPane scorePane = stripedStackPane(score, i, Pos.CENTER);
      StackPane killsPane = stripedStackPane(kills, i, Pos.CENTER);
      StackPane overKillsPane = stripedStackPane(overKills, i, Pos.CENTER);

      GridPane.setColumnIndex(positionPane, 0);
      GridPane.setRowIndex(positionPane, i + 1);
      GridPane.setColumnIndex(playerNamePane, 1);
      GridPane.setRowIndex(playerNamePane, i + 1);
      GridPane.setColumnIndex(scorePane, 2);
      GridPane.setRowIndex(scorePane, i + 1);
      GridPane.setColumnIndex(killsPane, 3);
      GridPane.setRowIndex(killsPane, i + 1);
      GridPane.setColumnIndex(overKillsPane, 4);
      GridPane.setRowIndex(overKillsPane, i + 1);

      ranksTable.getChildren().add(positionPane);
      ranksTable.getChildren().add(playerNamePane);
      ranksTable.getChildren().add(scorePane);
      ranksTable.getChildren().add(killsPane);
      ranksTable.getChildren().add(overKillsPane);
    }
  }

}
