package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Event fired when the master player toggles Final Frenzy.
 */
public class FinalFrenzyToggleEvent implements Event {

  private static final long serialVersionUID = -2684483250916955093L;
  private final boolean enabled;

  public FinalFrenzyToggleEvent(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public EventType getEventType() {
    return EventType.FINAL_FRENZY_TOGGLE_EVENT;
  }
}
