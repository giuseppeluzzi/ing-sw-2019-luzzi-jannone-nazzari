package it.polimi.se2019.adrenalina.controller.event;

public class MapSelectionEvent implements Event {

  private static final long serialVersionUID = -8427415116539500528L;
  private final int map;

  public MapSelectionEvent(int map) {
    this.map = map;
  }

  public int getMap() {
    return map;
  }

  @Override
  public EventType getEventType() {
    return EventType.MAP_SELECTION_EVENT;
  }
}
