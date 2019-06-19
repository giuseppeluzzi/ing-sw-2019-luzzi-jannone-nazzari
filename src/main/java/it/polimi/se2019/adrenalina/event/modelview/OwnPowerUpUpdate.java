package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event sent when the list of powerUps of a player changes. This event is only sent to the player itself.
 */
public class OwnPowerUpUpdate implements Event {

  private static final long serialVersionUID = 7471834268745266766L;
  private final PlayerColor playerColor;
  private final Map<PowerUpType, Map<AmmoColor, Integer>> powerUps;

  public OwnPowerUpUpdate(PlayerColor playerColor, List<PowerUp> powerUpList) {
    this.playerColor = playerColor;
    powerUps = new HashMap<>();
    for (PowerUp powerUp2 : powerUpList) {
      if (powerUps.containsKey(powerUp2.getType())) {
        if (powerUps.get(powerUp2.getType()).containsKey(powerUp2.getColor())) {
          int value = powerUps.get(powerUp2.getType()).get(powerUp2.getColor()) + 1;
          powerUps.get(powerUp2.getType()).put(powerUp2.getColor(), value);
        } else {
          powerUps.get(powerUp2.getType()).put(powerUp2.getColor(), 1);
        }
      } else {
        Map<AmmoColor, Integer> value = new HashMap<>();
        value.put(powerUp2.getColor(), 1);
        powerUps.put(powerUp2.getType(), value);
      }
    }
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public Map<PowerUpType, Map<AmmoColor, Integer>> getPowerUps() {
    return new HashMap<>(powerUps);
  }


  @Override
  public EventType getEventType() {
    return EventType.OWN_POWER_UP_UPDATE;
  }

  @Override
  public PlayerColor getPrivatePlayerColor() {
    return getPlayerColor();
  }
}
