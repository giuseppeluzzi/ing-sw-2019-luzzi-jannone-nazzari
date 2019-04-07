package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.controller.event.DoubleKillEvent;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.KillShotEvent;
import it.polimi.se2019.adrenalina.controller.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.controller.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.MessageSeverity;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class BoardView extends Observable implements Observer {
  private Board board;

  public BoardView(Board board) {
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public void startTimer(int time) {
    // TODO: show and start the countdown
  }

  public void hideTimer(int time) {
    // TODO: hide and stop the countdown
  }

  public void showMessage(MessageSeverity severity, String title, String message) {
    // TODO: show a message
  }

  public void reset() {
    // TODO: reset the board to the initial state
  }

  public void update(WeaponUpdateEvent event) {
    // TODO: replace a weapon on the board
  }

  public void update(AmmoCardUpdateEvent event) {
    // TODO: replace an ammo card on the board
  }

  public void update(KillShotEvent event) {
    // TODO: mark a new killshot
  }

  public void update(DoubleKillEvent event) {
    // TODO: mark a double kill
  }

  public void update(SpawnPointDamageEvent event) {
    // TODO: mark a damage to a spawn point (in domination mode)
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
