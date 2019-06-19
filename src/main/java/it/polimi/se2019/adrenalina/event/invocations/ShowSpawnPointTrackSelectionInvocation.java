package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Invocation to show the spawn point track selection on the client in case of overkill in domination mode.
 */
public class ShowSpawnPointTrackSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 8163442106943343588L;

  @Override
  public EventType getEventType() {
    return EventType.SHOW_SPAWN_POINT_TRACK_SELECTION_INVOCATION;
  }
}
