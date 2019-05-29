package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import java.io.Serializable;
import java.util.HashMap;

public interface Buyable extends Serializable {

  BuyableType getBuyableType();

  default HashMap<AmmoColor, Integer> getCost() {
    HashMap<AmmoColor, Integer> costs = new HashMap<>();
    for (AmmoColor color : AmmoColor.values()) {
      costs.put(color, getCost(color));
    }
    return costs;
  }

  int getCost(AmmoColor ammoColor);

  void afterPaymentCompleted(TurnController turnController, Board board, Player player);
}
