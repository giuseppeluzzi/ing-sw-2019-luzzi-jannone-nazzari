package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.ShootAction;

/**
 * Class defining a Tagback Grenade powerup card.
 */
public class TagbackGrenade extends PowerUp {

  private static final long serialVersionUID = 8370118473308407561L;

  public TagbackGrenade(AmmoColor color) {
    super(color, false, PowerUpType.TAGBACK_GRANADE);
    addAction(new ShootAction(1,0,1));
  }

  @Override
  public TagbackGrenade copy() {
    return new TagbackGrenade(this.getColor());
  }

  @Override
  public String getName() {
    return "Granata venom";
  }

  @Override
  public String getSymbol() {
    return "J";
  }
}