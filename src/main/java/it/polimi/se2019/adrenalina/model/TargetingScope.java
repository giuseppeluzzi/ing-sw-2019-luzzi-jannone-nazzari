package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.SelectAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;

/**
 * Class defining a Targeting Scoper powerup card.
 */
public class TargetingScope extends PowerUp {

  private static final long serialVersionUID = -9185952826985193602L;

  public TargetingScope(AmmoColor color) {
    super(color, true, PowerUpType.TARGETING_SCOPE);
    addAction(new SelectAction(0,1, TargetType.ATTACK_TARGET)
        .setMinDistance(0)
        .setMaxDistance(-1)
        .setDifferentFrom(0)
        .setBetween(1, 2, 3, 4, 5, 6, 7, 8));
    addAction(new ShootAction(1, 1, 0, true));
  }

  @Override
  public String getName() {
    return "Mirino";
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    if (ammoColor == AmmoColor.ANY) {
      return 1;
    }
    return 0;
  }

  @Override
  public TargetingScope copy() {
    return new TargetingScope(getColor());
  }

  @Override
  public String getSymbol() {
    return "Q";
  }
}
