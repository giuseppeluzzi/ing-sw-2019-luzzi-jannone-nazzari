package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PlayerCollectPowerUpEvent {
  private final Player player;
  private final PowerUp powerUp;

  public PlayerCollectPowerUpEvent(Player player, PowerUp powerUp) {
    this.player = player;
    this.powerUp = powerUp;
  }

  public Player getPlayer() {
    return player;
  }

  public PowerUp getPowerUp() {
    return powerUp;
  }
}
