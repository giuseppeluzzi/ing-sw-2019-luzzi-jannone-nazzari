package it.polimi.se2019.adrenalina.controller.action.game;

import it.polimi.se2019.adrenalina.controller.Configuration;
import it.polimi.se2019.adrenalina.controller.PlayerController;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.TurnController;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;

import it.polimi.se2019.adrenalina.utils.ANSIColor;
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
      PlayerController.sendMessageAllClients(player,
          String.format("%s%s%s è morto", player.getColor().getAnsiColor(), player.getName(),
          ANSIColor.RESET), board);
      if (isSync()) {
        try {
          board.getPlayerByColor(player.getDamages().get(Configuration.getInstance().getDeathDamages()))
              .getClient().getBoardView().showSpawnPointTrackSelection(((DominationBoard) board).getSpawnPointDamages());
        } catch (RemoteException e) {
          Log.exception(e);
        } catch (InvalidPlayerException ignore) {
          //
        }
        if (player.getStatus() != PlayerStatus.DISCONNECTED
            && player.getStatus() != PlayerStatus.SUSPENDED) {
          getTurnController().addRespawn(player);
        }
      } else {
        if (player.getStatus() != PlayerStatus.DISCONNECTED
            && player.getStatus() != PlayerStatus.SUSPENDED) {
          getTurnController().addRespawn(player);
        }
      }
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
    if (getTurnController().getBoardController().getBoard().isDominationBoard()) {
      return getTurnController().getBoardController().getBoard().existsOverKilledPlayer();
    }
    return false;
  }
}
