package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.exceptions.InputCancelledException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;

public class SelectEffect extends GameAction {

  public SelectEffect(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    try {
      getPlayer().getClient().getPlayerDashboardsView().showEffectSelection((Weapon) getPlayer()
          .getCurrentExecutable(),
          ((Weapon) getPlayer().getCurrentExecutable()).getEffects());
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }
}
