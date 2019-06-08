package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;

public class EndGame extends GameActionAsync {

  public EndGame() {
    super(null);
  }

  @Override
  public void execute(Board board) {
    for (Player player : board.getPlayers()) {
      try {
        player.getClient().showGameMessage("Partita terminata icao");
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }
}
