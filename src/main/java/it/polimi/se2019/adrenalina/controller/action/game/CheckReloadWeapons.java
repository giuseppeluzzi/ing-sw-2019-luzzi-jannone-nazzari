package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Action used to ask player to reload weapons he can reload.
 */
public class CheckReloadWeapons extends GameAction {


  public CheckReloadWeapons(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {
    if (!getReloadableWeapons().isEmpty()) {
      try {
        getPlayer().getClient().getPlayerDashboardsView().showReloadWeaponSelection(getReloadableWeapons());
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }


  List<Weapon> getReloadableWeapons() {
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

  @Override
  public boolean isSync() {
    return !getReloadableWeapons().isEmpty();
  }
}
