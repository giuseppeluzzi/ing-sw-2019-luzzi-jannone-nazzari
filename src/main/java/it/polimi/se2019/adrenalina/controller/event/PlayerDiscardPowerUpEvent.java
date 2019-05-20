package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PlayerDiscardPowerUpEvent implements Event {

  private static final long serialVersionUID = -4129538766030971119L;
  private final PlayerColor playerColor;
  private final PowerUp powerUp;

  public PlayerDiscardPowerUpEvent(PlayerColor playerColor,
      PowerUp powerUp) {
    this.playerColor = playerColor;
    this.powerUp = powerUp;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public PowerUp getPowerUp() {
    return powerUp;
  }
}
