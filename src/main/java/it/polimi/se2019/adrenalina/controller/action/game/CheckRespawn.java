package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;

public class CheckRespawn extends GameActionAsync {

  public CheckRespawn(Player player) {
    super(player);
  }

  @Override
  public void execute(Board board) {
    for (Player player : board.getPlayers()) {
      if (player.isDead()) {
        player.respawn();
      }
    }
  }
}
