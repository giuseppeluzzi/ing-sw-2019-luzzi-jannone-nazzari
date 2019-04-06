package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Square;

public class PlayerSpawnEvent implements Event {
  private final Player player;
  private final Square spawnLocation;
  private final PowerUp tossedPowerUp;

  public PlayerSpawnEvent(Player player, Square spawnLocation,
      PowerUp tossedPowerUp) {
    this.player = player;
    this.spawnLocation = spawnLocation;
    this.tossedPowerUp = tossedPowerUp;
  }

  public Player getPlayer() {
    return player;
  }

  public Square getSpawnLocation() {
    return spawnLocation;
  }

  public PowerUp getTossedPowerUp() {
    return tossedPowerUp;
  }
}
