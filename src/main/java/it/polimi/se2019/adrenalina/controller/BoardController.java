package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;

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
    // TODO: add a player to the board or change his state to playing if he was already in game
  }

  void disconnectPlayer(Player player) {
    // TODO: remove a player if the game is in lobby otherwise change his state to disconnected
  }

  @Override
  public void run() {
    // TODO: game manager
  }
}
