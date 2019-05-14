package it.polimi.se2019.adrenalina.controller.event;

public class MapSelectionEvent implements Event {

  private static final long serialVersionUID = -1952284048993470635L;
  private final int map;

  public MapSelectionEvent(int map) {
    this.map = map;
  }

  public int getMap() {
    return map;
  }
}
