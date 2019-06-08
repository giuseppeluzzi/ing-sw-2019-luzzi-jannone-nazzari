package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class DiscardPowerUp extends GameActionAsync {

  private final PowerUp powerUp;

  public DiscardPowerUp(TurnController turnController,
      Player player,
      PowerUp powerUp) {
    super(turnController, player);
    this.powerUp = powerUp;
  }

  @Override
  public void execute(Board board) {
    try {
      getPlayer().removePowerUp(powerUp);
    } catch (InvalidPowerUpException ignore) {
      //
    }
    board.undrawPowerUp(powerUp);
  }
}
