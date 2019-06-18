package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 * Invocation that has the client show the powerUp selection for (re)spawns.
 * @see Invocation
 */
public class ShowPowerUpSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 9171282818942368354L;
  private final List<PowerUp> powerUps;
  private final boolean discard;

  public ShowPowerUpSelectionInvocation(List<PowerUp> powerUps, boolean discard) {
    this.powerUps = new ArrayList<>(powerUps);
    this.discard = discard;
  }

  public boolean isDiscard() {
    return discard;
  }

  public List<PowerUp> getPowerUps() {
    return new ArrayList<>(powerUps);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_POWER_UP_SELECTION_INVOCATION;
  }
}
