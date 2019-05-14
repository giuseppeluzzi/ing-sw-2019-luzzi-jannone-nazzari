package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.exceptions.InvalidSquareException;

public interface Target {
  boolean isPlayer();
  Square getSquare();
  Player getPlayer() throws InvalidSquareException;
}
