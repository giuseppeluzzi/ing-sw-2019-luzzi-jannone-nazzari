package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;

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
    if (! weapon.isCancelled()) {
      try {
        Log.debug("WA: " + weaponAction.getActionType());
        weaponAction.execute(board, weapon);
      } catch (NoTargetsException e) {
        if (e.isRollback()) {
          getTurnController().addTurnActions(new MoveRollback(getTurnController(), getPlayer(), weapon));
        }
        weapon.setCancelled();
      }
    }
  }

  @Override
  public boolean isSync() {
    return weaponAction.isSync();
  }
}
