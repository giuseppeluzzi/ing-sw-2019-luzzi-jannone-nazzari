package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
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
    addAction(new SelectAction(0,1, 0, -1, new int[]{0},
       new int[]{}, null, false, false, false,
        TargetType.ATTACK_TARGET, false));
    addAction(new SelectDirectionAction());
    addAction(new SelectAction(1, 2, 0, 2, new int[]{},
        new int[]{}, null, false, true, false,
        TargetType.MOVE_SQUARE, false));
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

  /**
   * Creates Newton object from a JSON serialized object.
   * @param json JSON input String
   * @return Netwon object
   */
  public static Newton deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
    Gson gson = new Gson();
    return gson.fromJson(json, Newton.class);
  }

  @Override
  public Buyable getBaseBuyable() {
    return null;
  }
}
