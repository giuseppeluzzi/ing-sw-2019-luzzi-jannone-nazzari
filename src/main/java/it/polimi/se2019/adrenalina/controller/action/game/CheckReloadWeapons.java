package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.model.WeaponReload;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Action used to ask player to reload weapons he can reload.
 */
public class CheckReloadWeapons extends GameActionAsync {


  public CheckReloadWeapons(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {
    if (!getReloadableWeapons().isEmpty()) {
      List<GameAction> actions = new ArrayList<>();
      for (Weapon weapon : getReloadableWeapons()) {
        actions.add(new Payment(getTurnController(), getPlayer(), new WeaponReload(weapon)));
      }
      getTurnController().addTurnActions(actions);
      /*try {
        getPlayer().getClient().getPlayerDashboardsView()
            .showReloadWeaponSelection(getReloadableWeapons());
      } catch (RemoteException e) {
        Log.exception(e);
      }*/
    }
  }


  private List<Weapon> getReloadableWeapons() {
    List<Weapon> reloadableWeapons = new ArrayList<>();
    if (getPlayer().getUnloadedWeapons().isEmpty()) {
      return reloadableWeapons;
    }

    reloadableWeapons.addAll(getPlayer().getUnloadedWeapons());
    for (Weapon weapon : new ArrayList<>(reloadableWeapons)) {
      if (! getPlayer().canReload(weapon)) {
        reloadableWeapons.remove(weapon);
      }
    }
    return reloadableWeapons;
  }
}
