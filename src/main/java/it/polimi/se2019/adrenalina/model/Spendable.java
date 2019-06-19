package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;

/**
 * Any object that can be used as a currency for buying something.
 */
public interface Spendable {

  AmmoColor getColor();

  String getSpendableName();

  boolean isPowerUp();
}
