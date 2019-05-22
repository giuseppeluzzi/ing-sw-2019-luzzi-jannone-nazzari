package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;

public class CheckReloadWeapons extends GameAction {


  public CheckReloadWeapons(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {
    for (Weapon weapon : getPlayer().getWeapons()) {
      if (!weapon.isLoaded()) {
        getTurnController().addTurnActions(new ReloadWeapon(getPlayer(), weapon));
      }
    }
  }
}
