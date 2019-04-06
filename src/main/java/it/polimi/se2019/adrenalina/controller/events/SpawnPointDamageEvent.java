package it.polimi.se2019.adrenalina.controller.events;

import it.polimi.se2019.adrenalina.model.AmmoColor;
import it.polimi.se2019.adrenalina.model.Player;

public class SpawnPointDamageEvent implements Event {
  private final Player player;
  private final AmmoColor ammoColor;

  public SpawnPointDamageEvent(Player player, AmmoColor ammoColor) {
    this.player = player;
    this.ammoColor = ammoColor;
  }

  public Player getPlayer() {
    return player;
  }

  public AmmoColor getAmmoColor() {
    return ammoColor;
  }
}
