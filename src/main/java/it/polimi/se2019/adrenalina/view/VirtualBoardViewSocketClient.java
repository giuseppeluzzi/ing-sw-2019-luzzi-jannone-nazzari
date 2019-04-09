package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.controller.event.DoubleKillEvent;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.KillShotEvent;
import it.polimi.se2019.adrenalina.controller.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.controller.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.MessageSeverity;
import java.lang.invoke.WrongMethodTypeException;

public class VirtualBoardViewSocketClient extends BoardView {

  protected VirtualBoardViewSocketClient(Board board) {
    super(board);
  }

  @Override
  public void startTimer(int time) {
    // TODO: show and start the countdown
  }

  @Override
  public void hideTimer(int time) {
    // TODO: hide and stop the countdown
  }

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    // TODO: show a message
  }

  @Override
  public void reset() {
    // TODO: reset the board to the initial state
  }

  @Override
  public void update(WeaponUpdateEvent event) {
    // TODO: replace a weapon on the board
  }

  @Override
  public void update(AmmoCardUpdateEvent event) {
    // TODO: replace an ammo card on the board
  }

  @Override
  public void update(KillShotEvent event) {
    // TODO: mark a new killshot
  }

  @Override
  public void update(DoubleKillEvent event) {
    // TODO: mark a double kill
  }

  @Override
  public void update(SpawnPointDamageEvent event) {
    // TODO: mark a damage to a spawn point (in domination mode)
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}
