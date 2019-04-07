package it.polimi.se2019.adrenalina.model;

import java.util.ArrayList;
import java.util.List;

public class DominationBoard extends Board {
  private final List<Kill> blue;
  private final List<Kill> red;
  private final List<Kill> yellow;

  public DominationBoard() {
    blue = new ArrayList<>();
    red = new ArrayList<>();
    yellow = new ArrayList<>();
  }

  public DominationBoard(DominationBoard dominationBoard, boolean publicCopy) {
    // TODO: create a copy
    super(dominationBoard, publicCopy);
    blue = new ArrayList<>();
    red = new ArrayList<>();
    yellow = new ArrayList<>();
  }

  public void addBlueKill(Kill kill) {
    blue.add(kill);
  }

  public List<Kill> getBlue() {
    // TODO: blue is mutable
    return new ArrayList<>();
  }

  public void addRedKill(Kill kill) {
    blue.add(kill);
  }

  public List<Kill> getRed() {
    // TODO: red is mutable
    return new ArrayList<>();
  }

  public void addYellowKill(Kill kill) {
    blue.add(kill);
  }

  public List<Kill> getYellow() {
    // TODO: yellow is mutable
    return new ArrayList<>();
  }

  @Override
  public boolean isDominationBoard() {
    return true;
  }
}
