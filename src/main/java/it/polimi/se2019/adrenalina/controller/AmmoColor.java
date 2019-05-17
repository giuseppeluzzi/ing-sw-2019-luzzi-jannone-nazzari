package it.polimi.se2019.adrenalina.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of all possible colors of an ammo
 */
public enum AmmoColor {
  BLUE,
  RED,
  YELLOW,
  ANY;

  public static List<AmmoColor> getValidColor() {
    List<AmmoColor> list = new ArrayList<>();
    list.add(BLUE);
    list.add(RED);
    list.add(YELLOW);
    return list;
  }
}
