package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import java.util.ArrayList;

public class CheckRespawn extends GameActionAsync {

  public CheckRespawn(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {
    for (Player player : board.getPlayers()) {
      if (player.isDead()) {
        getTurnController().addRespawn(player);
      }
    }
  }
}
