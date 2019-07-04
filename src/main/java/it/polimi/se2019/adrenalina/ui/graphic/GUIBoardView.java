package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.modelview.BoardAddPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasWeaponsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardKillShotsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardRemovePlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.DominationBoardDamagesUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerMasterUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareAmmoCardUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareWeaponUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.ui.graphic.controller.FinalRanksFXController;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogDisconnectWarning;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogSelectDirection;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogSpawnPointTrackSelection;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class GUIBoardView extends BoardView {

  private static final long serialVersionUID = -5469323461908447838L;

  public GUIBoardView(Client client) {
    super(client, new GUITimer());
  }

  @Override
  public GUITimer getTimer() {
    return (GUITimer) super.getTimer();
  }

  @Override
  public void endLoading(boolean masterPlayer) {
    new Thread(() -> {
      try {
        Thread.sleep((long) Constants.PING_INTERVAL * 2);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      Platform.runLater(() -> AppGUI.getLobbyFXController().endLoading(masterPlayer));
    }).start();
  }

  @Override
  public void cancelInput() {
    // do nothing in GUI
  }

  @Override
  public void showBoard() {
    // TODO
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets, boolean skippable) {
    if (type == TargetType.ATTACK_TARGET) {
      AppGUI.getBoardFXController().enableTargetSelection(targets, skippable);
    } else {
      AppGUI.getBoardFXController().enableSquareSelection(type, targets, false, skippable);
    }
  }

  @Override
  public void showDirectionSelect() {
    final DialogSelectDirection dialogSelectDirection = new DialogSelectDirection();

    AppGUI.getBoardFXController().startTurnTimer(dialogSelectDirection);
    dialogSelectDirection.show();
  }

  @Override
  public void showSquareSelect(List<Target> targets) {
    AppGUI.getBoardFXController().startTurnTimer();
    AppGUI.getBoardFXController()
        .enableSquareSelection(TargetType.MOVE_SQUARE, targets, true, false);
  }

  @Override
  public void showBuyableWeapons(List<Weapon> weapons) {
    AppGUI.getBoardFXController().startTurnTimer();
    AppGUI.getBoardFXController().enableBoardWeapons(weapons);
  }

  @Override
  public void showSpawnPointTrackSelection(Map<AmmoColor, Integer> damages) {
    final DialogSpawnPointTrackSelection dialogSpawnPointTrackSelection = new DialogSpawnPointTrackSelection();

    AppGUI.getBoardFXController().startTurnTimer(dialogSpawnPointTrackSelection);
    dialogSpawnPointTrackSelection.show();
  }

  @Override
  public void showFinalRanks() {
    FXMLLoader loaderRanks = new FXMLLoader(
        AppGUI.class.getClassLoader().getResource("gui/FinalRanks.fxml"));
    loaderRanks.setController(new FinalRanksFXController());

    Platform.runLater(() -> {
      Scene ranksScene;
      try {
        ranksScene = new Scene(loaderRanks.load());
      } catch (IOException e) {
        Log.exception(e);
        return ;
      }

      ranksScene.getStylesheets().addAll(AppGUI.getCSS());
      AppGUI.getStage().setScene(ranksScene);
    });
  }

  @Override
  public void showDisconnectWarning() {
    new DialogDisconnectWarning().show();
  }

  @Override
  public void update(BoardStatusUpdate event) {
    super.update(event);
    if (event.getStatus() == BoardStatus.MATCH) {
      AppGUI.getLobbyFXController().closeChangeColorDialog();
      Platform.runLater(() -> AppGUI.getStage().setScene(AppGUI.getBoardScene()));
    }
  }

  @Override
  public void update(BoardAddPlayerUpdate event) {
    super.update(event);
    AppGUI.getLobbyFXController()
        .addPlayer(new Player(event.getPlayerName(), event.getPlayerColor(), getBoard()));

    try {
      if (event.getPlayerColor() == AppGUI.getClient().getPlayerColor()) {
        AppGUI.getBoardFXController().loadPlayerDashboard(AppGUI.getClient().getPlayerColor());
      } else {
        AppGUI.getBoardFXController().loadEnemyDashboard(event.getPlayerColor());
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void update(BoardRemovePlayerUpdate event) {
    super.update(event);
    AppGUI.getLobbyFXController().removePlayer(event.getPlayerColor());

    try {
      if (event.getPlayerColor() != AppGUI.getClient().getPlayerColor()) {
        AppGUI.getBoardFXController().unloadEnemyDashboard(event.getPlayerColor());
      }
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  @Override
  public void update(PlayerMasterUpdate event) {
    super.update(event);
    if (event.isMaster()) {
      AppGUI.getLobbyFXController().setPlayerMaster(event.getPlayerColor());
    }
  }

  @Override
  public void update(MapSelectionEvent event) {
    super.update(event);
    AppGUI.getLobbyFXController().setMapId(event.getMap());
    AppGUI.getBoardFXController().setMapId(event.getMap());
  }

  @Override
  public void update(PlayerColorSelectionEvent event) {
    super.update(event);
    AppGUI.getLobbyFXController().setPlayerColor(event.getPlayerColor(), event.getNewPlayerColor());
    AppGUI.getBoardFXController()
        .changeDashboardColor(event.getPlayerColor(), event.getNewPlayerColor());

  }

  @Override
  public void update(BoardSkullsUpdate event) {
    super.update(event);
    AppGUI.getLobbyFXController().setSkulls(event.getSkulls());
    AppGUI.getBoardFXController().loadBoardSkulls(event.getSkulls());
  }

  @Override
  public void update(SquareAmmoCardUpdate event) {
    super.update(event);
    String ammoCardStr;
    if (event.getBlue() == 0 && event.getRed() == 0 && event.getYellow() == 0
        && event.getPowerUps() == 0) {
      ammoCardStr = null;
    } else {
      AmmoCard ammoCard = new AmmoCard(event.getRed(), event.getBlue(), event.getYellow(),
          event.getPowerUps());
      ammoCardStr = ammoCard.toString();
    }
    Platform.runLater(() -> AppGUI.getBoardFXController()
        .setAmmoCard(event.getPosX(), event.getPosY(), ammoCardStr));
  }

  @Override
  public void update(SquareWeaponUpdate event) {
    super.update(event);

    Square square = getBoard().getSquare(event.getPosX(), event.getPosY());
    switch (square.getColor()) {
      case BLUE:
        AppGUI.getBoardFXController().updateBlueWeapons(square.getWeapons());
        break;
      case YELLOW:
        AppGUI.getBoardFXController().updateYellowWeapons(square.getWeapons());
        break;
      case RED:
        AppGUI.getBoardFXController().updateRedWeapons(square.getWeapons());
        break;
      default:
        throw new IllegalStateException("Illegal square color");
    }
  }

  public void update(BoardHasWeaponsUpdate event) {
    AppGUI.getBoardFXController().showBoardWeaponsDeck(event.hasWeapons());
  }

  @Override
  public void update(BoardKillShotsUpdate event) {
    super.update(event);
    AppGUI.getBoardFXController().updateKilltrack(event.getPlayers(), getBoard().getSkulls());
  }

  @Override
  public void update(DominationBoardDamagesUpdate event) {
    super.update(event);
    AppGUI.getBoardFXController()
        .updateSpawnpointDamages(event.getSpawnPointColor(), event.getPlayers());
  }
}
