package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.model.Player;

public class PlayerUpdateEvent implements Event {
  private final Player player;

  public PlayerUpdateEvent(Player player) {
    this.player = player;
  }

  @Override
  public String getEventName() {
    return "PlayerUpdate";
  }

  public Player getPlayer() {
    return player;
  }
}
