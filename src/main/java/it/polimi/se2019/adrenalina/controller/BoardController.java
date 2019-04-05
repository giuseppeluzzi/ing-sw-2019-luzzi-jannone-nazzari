package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.events.Event;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class BoardController implements Runnable, Observer {
  private Board board;
  private AttackController attackController;
  private PlayerController playerController;

  BoardController(boolean domination) {
    if (domination) {
      board = new DominationBoard();
    } else {
      board = new Board();
    }
    attackController = new AttackController(board);
    playerController = new PlayerController(board);
  }

  Board getBoard() {
    return board;
  }

  void connectPlayer(Player player) {

  }

  void disconnectPlayer(Player player) {

  }

  AttackController getAttackController() {
    return attackController;
  }

  PlayerController getPlayerController() {
    return playerController;
  }

  @Override
  public void run() {
  }

  @Override
  public void update(Event event) throws WrongMethodTypeException {

  }
}
