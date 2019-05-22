package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SquareSelection extends GameAction {

  private final int maxDistance;

  public SquareSelection(Player player, int maxDistance) {
    super(player);
    this.maxDistance = maxDistance;
  }

  public int getMaxDistance() {
    return maxDistance;
  }

  @Override
  public void execute(Board board) {
    List<Target> targets = new ArrayList<>(getPlayer().getSquare().getSquaresInRange(1, maxDistance));
    try {
      getPlayer().getClient().getBoardView().showSquareSelect(targets);
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
