package it.polimi.se2019.adrenalina.ui.graphic;

import it.polimi.se2019.adrenalina.AppGUI;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.modelview.BoardAddPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardRemovePlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerMasterUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogSelectDirection;
import it.polimi.se2019.adrenalina.ui.graphic.controller.dialogs.DialogSpawnPointTrackSelection;
import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.view.BoardView;
import java.io.IOException;
import java.rmi.RemoteException;
import javafx.application.Platform;

import java.util.List;

public class GUIBoardView extends BoardView {

  private static final long serialVersionUID = -5469323461908447838L;

  public GUIBoardView(Client client) {
    super(client, new GUITimer(client));
  }

  @Override
  public void endLoading(boolean masterPlayer) {
    new Thread(() -> {
      try {
        Thread.sleep((long) Constants.PING_INTERVAL * 2);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      Platform.runLater(() -> {
        AppGUI.getLobbyFXController().endLoading(masterPlayer);
      });
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
    // TODO in board
  }

  @Override
  public void showDirectionSelect() {
    final DialogSelectDirection dialogSelectDirection = new DialogSelectDirection();
    dialogSelectDirection.show();
  }

  @Override
  public void showSquareSelect(List<Target> targets) {
    // TODO in board
  }

  @Override
  public void showBuyableWeapons(List<Weapon> weapons) {
    // TODO in board
  }

  @Override
  public void showSpawnPointTrackSelection() {
    new DialogSpawnPointTrackSelection().show();
  }

  @Override
  public void showFinalRanks() {
    // TODO
  }

  @Override
  public void update(BoardStatusUpdate event) {
    super.update(event);
    if (event.getStatus() == BoardStatus.MATCH) {
      Platform.runLater(() -> {
        try {
          AppGUI.getStage().setScene(AppGUI.getBoardScene());
        } catch (IOException e) {
          Log.exception(e);
          e.printStackTrace();
        }
      });
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
    AppGUI.getBoardFXController().changeDashboardColor(event.getPlayerColor(), event.getNewPlayerColor());

  }

  @Override
  public void update(BoardSkullsUpdate event) {
    super.update(event);
    AppGUI.getLobbyFXController().setSkulls(event.getSkulls());
  }
}
