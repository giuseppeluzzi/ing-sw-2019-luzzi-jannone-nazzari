package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PowerUpEffect extends GameAction {

  private final PowerUp powerUp;
  private final WeaponAction weaponAction;

  public PowerUpEffect(Player player, PowerUp powerUp, WeaponAction weaponAction) {
    super(player);
    this.powerUp = powerUp;
    this.weaponAction = weaponAction;
  }

  public PowerUp getPowerUp() {
    return powerUp;
  }

  public WeaponAction getWeaponAction() {
    return weaponAction;
  }

  @Override
  public void execute(Board board) {
    weaponAction.execute(board, powerUp);
  }

  @Override
  public boolean isSync() {
    return weaponAction.isSync();
  }
}
