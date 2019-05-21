package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;

public interface Spendable {

  AmmoColor getColor();

  String getSpendableName();

  boolean isPowerUp();
}
