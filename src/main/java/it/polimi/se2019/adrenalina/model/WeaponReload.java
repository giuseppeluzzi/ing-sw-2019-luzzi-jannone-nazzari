package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;

import java.util.Map;

public class WeaponReload implements Buyable {

  private static final long serialVersionUID = -7744703960026899472L;
  private final Weapon weapon;

  public WeaponReload(Weapon weapon) {
    this.weapon = weapon;
  }

  @Override
  public BuyableType getBuyableType() {
    return BuyableType.WEAPON_RELOAD;
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    return weapon.getCost(ammoColor);
  }

  @Override
  public Buyable getBaseBuyable() {
    return weapon;
  }

  @Override
  public Map<AmmoColor, Integer> getCost() {
    return weapon.getCost(true);
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    board.getWeaponByName(weapon.getName()).setLoaded(true);
    turnController.executeGameActionQueue();
  }

  @Override
  public String promptMessage() {
    return weapon.getName() + " (ricarica)";
  }
}
