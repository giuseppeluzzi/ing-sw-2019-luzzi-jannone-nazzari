package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.exceptions.EndedGameException;
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
   * Adds a new player to a board in LOBBY status or a returning
   * player (who had previously disconnected) to a board where a game
   * is in progress.
   * @param player the player to be added.
   * @throws FullBoardException thrown if the board already has 5 players.
   * @throws PlayingBoardException thrown if the status of the board is not
   * LOBBY (a game is already in progress or ended) and the player does not
   * belong to that game.
   * @throws EndedGameException thrown if this board hosts a game which has
   * already ended.
   */
  void addPlayer(Player player) throws FullBoardException, PlayingBoardException, EndedGameException {
    if (board.getStatus() == BoardStatus.LOBBY) {
      if (board.getPlayers().size() < 5) {
        board.addPlayer(player);
        player.setStatus(PlayerStatus.WAITING);
      } else {
        throw new FullBoardException();
      }
    } else if (board.getStatus() == BoardStatus.END) {
      throw new EndedGameException();
    } else {
      if (board.getPlayers().contains(player)) {
        player.setStatus(PlayerStatus.PLAYING);
      } else {
        throw new PlayingBoardException("Board isn't in LOBBY status");
      }
    }
  }

  /**
   * Removes a player from a board in LOBBY status or sets the player's
   * status to DISCONNECTED if the game on that board is already in progress.
   * @param player the player to be removed.
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
