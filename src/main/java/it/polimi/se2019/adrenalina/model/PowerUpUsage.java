package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.controller.action.game.AfterUsageExecutable;
import it.polimi.se2019.adrenalina.controller.action.game.ExecutableEffect;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponAction;
import it.polimi.se2019.adrenalina.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Decorator for using a powerUp as a buyable.
 */
public class PowerUpUsage implements Buyable {

  private static final long serialVersionUID = 7357325998283241914L;
  private final PowerUp powerUp;

  public PowerUpUsage(PowerUp powerUp) {
    this.powerUp = powerUp;
  }

  @Override
  public BuyableType getBuyableType() {
    return powerUp.getBuyableType();
  }

  @Override
  public Map<AmmoColor, Integer> getCost() {
    return powerUp.getCost();
  }

  @Override
  public int getCost(AmmoColor ammoColor) {
    return powerUp.getCost(ammoColor);
  }

  @Override
  public void afterPaymentCompleted(TurnController turnController, Board board, Player player) {
    PowerUp playerPowerUp = player.getPowerUp(this.powerUp.getType(), this.powerUp.getColor());
    List<GameAction> actions = new ArrayList<>();

    for (WeaponAction action : playerPowerUp.getActions()) {
      actions.add(new ExecutableEffect(turnController, player, playerPowerUp, action));
      Log.println(action.getActionType().toString());
    }
    actions.add(new AfterUsageExecutable(turnController, player, playerPowerUp));
    turnController.addTurnActions(actions);
    turnController.executeGameActionQueue();
  }

  @Override
  public String promptMessage() {
    if (powerUp.getType() == PowerUpType.TARGETING_SCOPE) {
      return "il mirino";
    }
    return "";
  }
}
