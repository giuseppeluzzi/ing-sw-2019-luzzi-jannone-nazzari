package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import java.util.HashMap;

public class PowerUpDiscard implements Buyable {

  private static final long serialVersionUID = -1774317126276998750L;
  private final PowerUp powerUp;

  public PowerUpDiscard(PowerUp powerUp) {
    this.powerUp = powerUp;
  }

  @Override
  public BuyableType getBuyableType() {
    return powerUp.getBuyableType();
  }

  @Override
  public HashMap<AmmoColor, Integer> getCost() {
    return powerUp.getCost();
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    return powerUp.getCost(ammoColor);
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    PowerUp powerUp = player.getPowerUp(this.powerUp.getType(), this.powerUp.getColor());
    if (powerUp != null) {
      board.undrawPowerUp(powerUp);
      try {
        player.removePowerUp(powerUp);
      } catch (InvalidPowerUpException ignored) {
        // ignore exception
      }
    }
  }
}
