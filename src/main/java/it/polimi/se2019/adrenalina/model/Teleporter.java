package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.MoveAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.SelectAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;

/**
 * Class defining a Teleporter powerup card.
 */
public class Teleporter extends PowerUp {

  private static final long serialVersionUID = -384452700114270235L;


  public Teleporter(AmmoColor color) {
    super(color, false, PowerUpType.TELEPORTER);
    addAction(new SelectAction(0,1,0,-1, new int[]{},
        new int[]{}, null, false, false,
        false, TargetType.MOVE_SQUARE, false));
    addAction(new MoveAction(0, 1));
  }

  @Override
  public Teleporter copy() {
    return new Teleporter(getColor());
  }

  @Override
  public String getName() {
    return "Teletrasporto";
  }

  @Override
  public String getSymbol() {
    return "H";
  }
}