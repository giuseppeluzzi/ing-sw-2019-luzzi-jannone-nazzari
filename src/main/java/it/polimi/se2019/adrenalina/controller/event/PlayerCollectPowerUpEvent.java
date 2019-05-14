package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PlayerCollectPowerUpEvent implements Event {

  private static final long serialVersionUID = 5677407302226017103L;
  private final PlayerColor playerColor;
  private final PowerUp powerUp;

  public PlayerCollectPowerUpEvent(PlayerColor playerColor,
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
