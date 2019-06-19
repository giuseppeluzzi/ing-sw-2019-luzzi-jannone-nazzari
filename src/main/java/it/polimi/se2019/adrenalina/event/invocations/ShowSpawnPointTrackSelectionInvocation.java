package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;

/**
 * Invocation that has the client show the spawn point track selection in case of overkill in domination mode.
 * @see Invocation
 */
public class ShowSpawnPointTrackSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 8163442106943343588L;

  @Override
  public EventType getEventType() {
    return EventType.SHOW_SPAWN_POINT_TRACK_SELECTION_INVOCATION;
  }
}
