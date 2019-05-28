package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class OwnPowerUpUpdate implements Event {

  private static final long serialVersionUID = 7471834268745266766L;
  private final PlayerColor playerColor;
  private final Map<PowerUpType, AmmoColor> powerUps;

  public OwnPowerUpUpdate(PlayerColor playerColor, Map<PowerUpType, AmmoColor> powerUps) {
    this.playerColor = playerColor;
    this.powerUps = new EnumMap<>(powerUps);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public Map<PowerUpType, AmmoColor> getPowerUps() {
    return new EnumMap<>(powerUps);
  }

  @Override
  public EventType getEventType() {
    return EventType.OWN_POWER_UP_UPDATE;
  }
}
