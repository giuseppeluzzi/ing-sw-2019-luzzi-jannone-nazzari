package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;

public class WeaponEffect extends GameAction {

  private final Weapon weapon;
  private final WeaponAction weaponAction;

  public WeaponEffect(Player player, Weapon weapon, WeaponAction weaponAction) {
    super(player);
    this.weapon = weapon;
    this.weaponAction = weaponAction;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public WeaponAction getWeaponAction() {
    return weaponAction;
  }

  @Override
  public void execute(Board board) {
    weaponAction.execute(board, weapon);
  }

  @Override
  public boolean isSync() {
    return weaponAction.isSync();
  }
}
