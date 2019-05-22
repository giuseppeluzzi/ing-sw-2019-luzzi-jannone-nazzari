package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;

public class Payment extends GameAction {

  private final Buyable item;

  public Payment(TurnController turnController, Player player, Buyable item) {
    super(player);
    this.item = item;
  }

  public Buyable getItem() {
    return item;
  }

  @Override
  public void execute(Board board) {
    if (isSync()) {
      try {
        getPlayer().getClient().getPlayerDashboardsView()
            .showPaymentOption(item);
      } catch (RemoteException e) {
        Log.exception(e);
      }
    } else {
      item.afterPaymentCompleted(getTurnController(), board, getPlayer());
    }
  }

  @Override
  public boolean isSync() {
    return item.getCost(AmmoColor.RED) != 0 ||
        item.getCost(AmmoColor.BLUE) != 0 ||
        item.getCost(AmmoColor.YELLOW) != 0 ||
        item.getCost(AmmoColor.ANY) != 0;
  }
}
