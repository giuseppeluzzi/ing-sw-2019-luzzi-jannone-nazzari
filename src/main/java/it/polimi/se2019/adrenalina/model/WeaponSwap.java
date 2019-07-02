package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;

public class WeaponSwap implements Buyable {

  private static final long serialVersionUID = 5677212282456838114L;
  private final Weapon ownWeapon;
  private final Weapon boardWeapon;

  public WeaponSwap(Weapon ownWeapon, Weapon boardWeapon) {
    this.ownWeapon = ownWeapon;
    this.boardWeapon = boardWeapon;
  }

  public Weapon getOwnWeapon() {
    return ownWeapon;
  }

  public Weapon getBoardWeapon() {
    return boardWeapon;
  }

  @Override
  public BuyableType getBuyableType() {
    return BuyableType.WEAPON_SWAP;
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    return boardWeapon.getCost(ammoColor);
  }

  @Override
  public Buyable getBaseBuyable() {
    return boardWeapon;
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    player.removeWeapon(ownWeapon);
    player.getSquare().removeWeapon(boardWeapon);
    player.addWeapon(boardWeapon);
    player.getSquare().addWeapon(ownWeapon);
    turnController.executeGameActionQueue();
  }

  @Override
  public String promptMessage() {
    return boardWeapon.getName() + " (scambio)";
  }
}
