package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Action used to select a weapon to use.
 */
public class SelectWeapon extends GameAction {

  public SelectWeapon(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    List<Weapon> weapons = getLoadedWeapons(getPlayer().getWeapons());
    try {
      getPlayer().getClient().getPlayerDashboardsView().showWeaponSelection(weapons);
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  static List<Weapon> getLoadedWeapons(List<Weapon> playerWeapons) {
    List<Weapon> weapons = new ArrayList<>();
    for (Weapon weapon : playerWeapons) {
      if (weapon.isLoaded()) {
        weapons.add(weapon);
      }
    }
    return weapons;
  }
}
