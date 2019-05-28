package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import java.util.HashMap;

public class PlayerWeaponUpdate implements Event {

  private static final long serialVersionUID = -4678079189320950629L;
  private final HashMap<String, Boolean> weaponsLoaded;
  private final PlayerColor playerColor;

  public PlayerWeaponUpdate(PlayerColor playerColor, HashMap<String, Boolean> weaponsLoaded) {
    this.weaponsLoaded = new HashMap<>(weaponsLoaded);
    this.playerColor = playerColor;
  }

  public HashMap<String, Boolean> getWeaponsLoaded() {
    return new HashMap<>(weaponsLoaded);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_WEAPON_UPDATE;
  }
}
