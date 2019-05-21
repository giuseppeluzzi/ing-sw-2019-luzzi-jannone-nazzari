package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PickPowerUp extends GameActionAsync {

  public PickPowerUp(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    PowerUp powerUp = board.getPowerUps().get(0);
    board.drawPowerUp(powerUp);
    try {
      getPlayer().addPowerUp(powerUp);
    } catch (InvalidPowerUpException e) {
      // ignore exception
    }
  }
}
