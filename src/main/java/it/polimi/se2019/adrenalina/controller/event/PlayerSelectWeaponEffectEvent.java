package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import java.util.ArrayList;
import java.util.List;

public class PlayerSelectWeaponEffectEvent implements Event {

  private static final long serialVersionUID = 6708580194992758891L;
  private final PlayerColor playerColor;
  private final String weaponName;
  private final List<String> effectNames;

  public PlayerSelectWeaponEffectEvent(PlayerColor playerColor, String weaponName, List<String> effectNames) {
    this.playerColor = playerColor;
    this.weaponName = weaponName;
    this.effectNames = new ArrayList<>(effectNames);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public String getWeaponName() {
    return weaponName;
  }

  public List<String> getEffectNames() {
    return new ArrayList<>(effectNames);
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_SELECT_WEAPON_EFFECT_EVENT;
  }
}