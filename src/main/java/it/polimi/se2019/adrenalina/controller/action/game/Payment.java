package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;

public class Payment extends GameAction {

  private final int redCost;
  private final int blueCost;
  private final int yellowCost;

  public Payment(Player player, int redCost, int blueCost, int yellowCost) {
    super(player);
    this.redCost = redCost;
    this.blueCost = blueCost;
    this.yellowCost = yellowCost;
  }

  public int getRedCost() {
    return redCost;
  }

  public int getBlueCost() {
    return blueCost;
  }

  public int getYellowCost() {
    return yellowCost;
  }

  @Override
  public void execute(Board board) {
    // TODO
  }
}
