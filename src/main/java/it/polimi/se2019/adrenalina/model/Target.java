package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;

import it.polimi.se2019.adrenalina.utils.ANSIColor;
import java.io.Serializable;

/**
 * Any object that can be shot.
 */
public interface Target extends Serializable {
  boolean isPlayer();
  Square getSquare();
  Player getPlayer() throws InvalidSquareException;
  void addDamages(PlayerColor player, int num, boolean powerup);
  void addTags(PlayerColor player, int num);
  void setSquare(Square square);
  ANSIColor getAnsiColor();
  String getName();
}
