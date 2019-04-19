package it.polimi.se2019.adrenalina.controller;

public interface Action {
  ActionType getActionType();

  String serialize();

  boolean equals(Object object);

  int hashCode();
}
