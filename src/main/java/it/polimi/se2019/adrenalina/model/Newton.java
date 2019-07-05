package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.MoveAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.SelectAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.SelectDirectionAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;

/**
 * Class describing a Newton powerUp card.
 */
public class Newton extends PowerUp {

  private static final long serialVersionUID = 3859349171755429567L;

  public Newton(AmmoColor color) {
    super(color, false, PowerUpType.NEWTON);
    addAction(new SelectAction(0,1, TargetType.ATTACK_TARGET)
        .setMinDistance(0)
        .setMaxDistance(-1)
        .setDifferentFrom(0)
        .setDisallowSpawnPoint(true));
    addAction(new SelectDirectionAction());
    addAction(new SelectAction(1, 2, TargetType.MOVE_SQUARE)
        .setMinDistance(0)
        .setMaxDistance(2)
        .setUseLastDirection(true));
    addAction(new MoveAction(1,2));
  }

  @Override
  public String getName() {
    return "Raggio cinetico";
  }

  /**
   * Returns an exact copy of this object.
   * @return an exact copy of this object
   */
  @Override
  public Newton copy() {
    return new Newton(getColor());
  }

  @Override
  public String getSymbol() {
    return "W";
  }
}
