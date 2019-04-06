package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.Player;

public class PlayerUpdateEvent implements Event {
  private final Player player;

  public PlayerUpdateEvent(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
