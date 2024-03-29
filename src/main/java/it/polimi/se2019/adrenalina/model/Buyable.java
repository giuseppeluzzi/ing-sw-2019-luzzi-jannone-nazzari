package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Any object that can be bought in exchange for something.
 */
public interface Buyable extends Serializable {

  BuyableType getBuyableType();

  default Map<AmmoColor, Integer> getCost() {
    Map<AmmoColor, Integer> costs = new HashMap<>();
    for (AmmoColor color : AmmoColor.values()) {
      costs.put(color, getCost(color));
    }
    return costs;
  }

  int getCost(AmmoColor ammoColor);

  /**
   * If used in decorator returns the corresponding object, otherwise it returns the object itself.
   *
   * @return a Buyable not decorated
   */
  Buyable getBaseBuyable();

  void afterPaymentCompleted(TurnController turnController, Board board, Player player);

  default String promptMessage() {
    return "";
  }
}
