package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;

public class PlayerMoveEvent implements Event {
  private final Player player;
  private final Square newLocation;

  public PlayerMoveEvent(Player player, Square newLocation) {
    this.player = player;
    this.newLocation = newLocation;
  }

  public Player getPlayer() {
    return player;
  }

  public Square getNewLocation() {
    return newLocation;
  }
}
