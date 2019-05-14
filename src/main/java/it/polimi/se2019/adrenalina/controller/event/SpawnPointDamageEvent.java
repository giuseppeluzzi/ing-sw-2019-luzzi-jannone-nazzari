package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import java.io.Serializable;

public class SpawnPointDamageEvent implements Event {

  private static final long serialVersionUID = 2800089775218160405L;
  private final PlayerColor playerColor;
  private final AmmoColor ammoColor;

  public SpawnPointDamageEvent(PlayerColor playerColor,
      AmmoColor ammoColor) {
    this.playerColor = playerColor;
    this.ammoColor = ammoColor;
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public AmmoColor getAmmoColor() {
    return ammoColor;
  }
}
