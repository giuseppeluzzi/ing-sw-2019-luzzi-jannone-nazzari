package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Action used to select a square.
 */
public class SquareSelection extends GameAction {

  private final int maxDistance;
  private final boolean fetch;

  public SquareSelection(Player player, int maxDistance, boolean fetch) {
    super(player);
    this.maxDistance = maxDistance;
    this.fetch = fetch;
  }

  public int getMaxDistance() {
    return maxDistance;
  }

  public boolean isFetch() {
    return fetch;
  }

  List<Target> getTargets() {
    return new ArrayList<>(getPlayer().getSquare().getSquaresInRange(0, maxDistance, fetch));
  }

  @Override
  public void execute(Board board) {
    List<Target> targets = getTargets();
    try {
      getPlayer().getClient().getBoardView().showSquareSelect(targets);
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
