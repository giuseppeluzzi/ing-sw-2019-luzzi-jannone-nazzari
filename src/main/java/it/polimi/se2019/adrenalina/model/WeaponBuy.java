package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;

import java.util.Map;

public class WeaponBuy implements Buyable {

  private static final long serialVersionUID = -7744703960026899472L;
  private final Weapon weapon;

  public WeaponBuy(Weapon weapon) {
    this.weapon = weapon;
  }

  @Override
  public BuyableType getBuyableType() {
    return weapon.getBuyableType();
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
    return weapon.getCost(false);
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    if (player.hasWeapon(weapon)) {
      weapon.setLoaded(true);
    } else {
      for (Square square : board.getSquares()) {
        if (square.getWeapons().contains(weapon)) {
          square.removeWeapon(weapon);
          break;
        }
      }
      player.addWeapon(weapon);
    }
    turnController.executeGameActionQueue();
  }

  @Override
  public String promptMessage() {
    return weapon.getName();
  }
}
