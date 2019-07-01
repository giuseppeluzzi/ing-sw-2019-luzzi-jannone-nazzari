package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;

import it.polimi.se2019.adrenalina.utils.Constants;
import it.polimi.se2019.adrenalina.utils.Log;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks if any player needs to respawn, in that case it allows them to do so.
 */
public class CheckRespawn extends GameAction {

  public CheckRespawn(TurnController turnController, Player player) {
    super(turnController, player);
  }

  @Override
  public void execute(Board board) {

    if (board.isDominationBoard() && getPlayer().getSquare().isSpawnPoint()
        && getPlayer().getSquare().getColor().getEquivalentAmmoColor() != null) {

      getPlayer().addDamages(getPlayer().getColor(), 1, false);

      addDominationDamages(getPlayer(), board);
    }

    for (Player player : getDeadPlayers(board)) {
      if (board.isDominationBoard() && player.getDamages().size() == Constants.OVERKILL_DEATH) {
        try {
          board.getPlayerByColor(player.getDamages().get(Constants.OVERKILL_DEATH-1))
              .getClient().getBoardView().showSpawnPointTrackSelection(((DominationBoard) board).getSpawnPointDamages());
        } catch (RemoteException e) {
          Log.exception(e);
        } catch (InvalidPlayerException ignore) {
          //
        }
      }
      getTurnController().addRespawn(player);
      getTurnController().executeGameActionQueue();
    }
  }

  /**
   * Adds damages for players who stay alone on a spawnPoint in domination mode.
   * @param currentPlayer the considered player
   */
  private void addDominationDamages(Player currentPlayer, Board board) {
    if (currentPlayer.getSquare().getPlayers().size() == 1) {
      ((DominationBoard) board)
          .addDamage(currentPlayer.getSquare().getColor().getEquivalentAmmoColor(),
              currentPlayer.getColor());
    }
  }

  static List<Player> getDeadPlayers(Board board) {
    List<Player> output = new ArrayList<>();
    for (Player player : board.getPlayers()) {
      if (player.isDead()) {
        player.setStatus(PlayerStatus.WAITING);
        output.add(player);
      }
    }
    return output;
  }

  @Override
  public boolean isSync() {
    return getTurnController().getBoardController().getBoard().existsKilledPlayer();
  }
}
