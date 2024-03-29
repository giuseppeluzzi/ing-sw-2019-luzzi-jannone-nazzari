package it.polimi.se2019.adrenalina.event.modelview;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 * Event sent when the list of powerUps of a player changes. This event is only sent to the player
 * itself.
 */
public class OwnPowerUpUpdate implements Event {

  private static final long serialVersionUID = 7471834268745266766L;
  private final PlayerColor playerColor;
  private final List<PowerUp> powerUps;

  public OwnPowerUpUpdate(PlayerColor playerColor, List<PowerUp> powerUps) {
    this.playerColor = playerColor;
    this.powerUps = new ArrayList<>(powerUps);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public List<PowerUp> getPowerUps() {
    return new ArrayList<>(powerUps);
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
