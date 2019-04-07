package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;

public class PlayerCollectPowerUpEvent implements Event{
  private final Player player;
  private final PowerUp powerUp;

  public PlayerCollectPowerUpEvent(Player player, PowerUp powerUp) {
    this.player = player;
    this.powerUp = powerUp;
  }

  @Override
  public String getEventName() {
    return "PlayerCollectPowerUp";
  }

  public Player getPlayer() {
    return player;
  }

  public PowerUp getPowerUp() {
    return powerUp;
  }
}
