package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.exceptions.FullBoardException;
import it.polimi.se2019.adrenalina.exceptions.PlayingBoardException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.BoardStatus;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PlayerStatus;

public class BoardController implements Runnable {
  private final Board board;
  private final AttackController attackController;
  private final PlayerController playerController;

  public BoardController(boolean domination) {
    if (domination) {
      board = new DominationBoard();
    } else {
      board = new Board();
    }
    attackController = new AttackController(this);
    playerController = new PlayerController(this);
  }

  public Board getBoard() {
    return board;
  }

  public AttackController getAttackController() {
    return attackController;
  }

  public PlayerController getPlayerController() {
    return playerController;
  }

  /**
   * Adds a new (existing) player to a board in lobby (playing) status
   * @param player player to be added
   * @throws FullBoardException if the board already has 5 players
   * @throws PlayingBoardException if the player is new and the match isn't in lobby status
   */
  void addPlayer(Player player) throws FullBoardException, PlayingBoardException {
    if (board.getStatus() == BoardStatus.LOBBY) {
      if (board.getPlayers().size() < 5) {
        board.addPlayer(player);
        player.setStatus(PlayerStatus.WAITING);
      } else {
        throw new FullBoardException();
      }
    } else {
      if (board.getPlayers().contains(player)) {
        player.setStatus(PlayerStatus.PLAYING);
      } else {
        throw new PlayingBoardException("Board isn't in lobby status");
      }
    }
  }

  /**
   * Removes a player from a board
   * @param player to be removed
   */
  void removePlayer(Player player) {
    if (board.getStatus() == BoardStatus.LOBBY) {
      board.removePlayer(player.getColor());
    } else {
      player.setStatus(PlayerStatus.DISCONNECTED);
    }
  }

  @Override
  public void run() {
    // TODO: game manager
  }
}
