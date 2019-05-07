package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Square;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameMap implements Serializable {

  private static final long serialVersionUID = 1066718572076893345L;
  private final int id;
  private final String name;
  private final String description;
  private final int minPlayers;
  private final int maxPlayers;
  private final ArrayList<Square> squares;

  public GameMap(int id, String name, String description, int minPlayers, int maxPlayers) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.minPlayers = minPlayers;
    this.maxPlayers = maxPlayers;
    squares = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getMinPlayers() {
    return minPlayers;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public List<Square> getSquares() {
    return new ArrayList<>(squares);
  }

}
