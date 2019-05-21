package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;

public class ReloadWeapon extends GameAction {

  private final Weapon weapon;

  public ReloadWeapon(Player player, Weapon weapon) {
    super(player);
    this.weapon = weapon;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  @Override
  public void execute(Board board) {
    // TODO
  }
}
