package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.BoardStatus;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PlayerStatus;

public class BoardController implements Runnable {
  private final Board board;
  private final AttackController attackController;
  private final PlayerController playerController;

  BoardController(boolean domination) {
    if (domination) {
      board = new DominationBoard();
    } else {
      board = new Board();
    }
    attackController = new AttackController(this);
    playerController = new PlayerController(this);
  }

  Board getBoard() {
    return board;
  }

  AttackController getAttackController() {
    return attackController;
  }

  PlayerController getPlayerController() {
    return playerController;
  }

  void connectPlayer(Player player) {
    if (board.getStatus() == BoardStatus.LOBBY) {
      board.addPlayer(player);
    } else {
      player.setStatus(PlayerStatus.PLAYING);
    }
  }

  void disconnectPlayer(Player player) {
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
