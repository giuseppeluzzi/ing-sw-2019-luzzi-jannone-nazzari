package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;

public class PowerUpSelection extends GameAction {

  public PowerUpSelection(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    try {
      getPlayer().getClient().getPlayerDashboardsView().showPowerUpSelection(getPlayer()
          .getPowerUps());
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
