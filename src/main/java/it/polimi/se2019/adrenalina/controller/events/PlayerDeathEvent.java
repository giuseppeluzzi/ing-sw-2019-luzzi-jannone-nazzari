package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.Player;

public class PlayerDeathEvent implements Event {
  private final Player player;
  private final Player killer;

  public PlayerDeathEvent(Player player, Player killer) {
    this.player = player;
    this.killer = killer;
  }

  public Player getPlayer() {
    return player;
  }

  public Player getKiller() {
    return killer;
  }
}
