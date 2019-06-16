package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class CheckRespawn extends GameActionAsync {

  private final boolean everyPlayer;

  public CheckRespawn(TurnController turnController, Player player, boolean everyPlayer) {
    super(turnController, player);
    this.everyPlayer = everyPlayer;
  }

  @Override
  public void execute(Board board) {
    if (!getPlayer().getUnloadedWeapons().isEmpty()) {
      List<Weapon> unloadedWeapons = getPlayer().getUnloadedWeapons();
      for (Weapon weapon : unloadedWeapons) {
        if (!getPlayer().canReload(weapon)) {
          unloadedWeapons.remove(weapon);
        }
      }
      try {
        getPlayer().getClient().getPlayerDashboardsView().showReloadWeaponSelection(unloadedWeapons);
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
    if (everyPlayer) {
      for (Player player : getDeadPlayers(board)) {
        getTurnController().addRespawn(player);
      }
    } else {
      if (getPlayer().isDead()) {
        getTurnController().addRespawn(getPlayer());
      }
    }
  }

  static List<Player> getDeadPlayers(Board board) {
    List<Player> output = new ArrayList<>();
    for (Player player : board.getPlayers()) {
      if (player.isDead()) {
        output.add(player);
      }
    }
    return output;
  }
}
