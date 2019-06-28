package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Action used to pay for a buyable object.
 */
public class Payment extends GameAction {

  private final Buyable item;

  public Payment(TurnController turnController, Player player, Buyable item) {
    super(turnController, player);
    this.item = item;
  }

  public Buyable getItem() {
    return item;
  }

  @Override
  public void execute(Board board) {
    Log.debug("Sono nella payment");
    if (! isEnabled()) {
      getTurnController().executeGameActionQueue();
    }
    if (isFree()) {
      Log.debug("Sono nella payment free");
      Log.debug("Il mio item è:" + item.getBuyableType());
      item.afterPaymentCompleted(getTurnController(), board, getPlayer());
    } else {
      Log.debug("Sono nella payment a pagamento");
      getPlayer().setCurrentBuying(item);
      try {
        List<PowerUp> spendablePowerUps =  getPlayer().getPowerUps();
        spendablePowerUps.remove(item.getBaseBuyable());
        if (getPlayer().getClient() != null) {
          getPlayer().getClient().getPlayerDashboardsView()
              .showPaymentOption(item.getBuyableType(), item.promptMessage(), item.getCost(),
                  spendablePowerUps,
                  getPlayer().getAmmos());
        }
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  boolean isFree() {
    return item.getBuyableType() != BuyableType.WEAPON_RELOAD
        && item.getCost(AmmoColor.RED) == 0
        && item.getCost(AmmoColor.BLUE) == 0
        && item.getCost(AmmoColor.YELLOW) == 0
        && item.getCost(AmmoColor.ANY) == 0;
  }
}
