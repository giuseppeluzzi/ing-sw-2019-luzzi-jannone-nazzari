package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;

/**
 * Action used to select a spawn point track.
 */
public class SpawnPointTrackSelection extends GameAction {

  public SpawnPointTrackSelection(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    try {
      getPlayer().getClient().getBoardView().showSpawnPointTrackSelection();
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
