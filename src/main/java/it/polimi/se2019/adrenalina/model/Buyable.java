package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.TurnController;
import java.io.Serializable;

public interface Buyable extends Serializable {

  BuyableType getBuyableType();

  int getCost(AmmoColor ammoColor);

  void afterPaymentCompleted(TurnController turnController, Board board, Player player);
}
