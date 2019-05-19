package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.action.TargetType;
import it.polimi.se2019.adrenalina.controller.event.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.controller.event.DoubleKillEvent;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.KillShotEvent;
import it.polimi.se2019.adrenalina.controller.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.controller.event.TimerSetEvent;
import it.polimi.se2019.adrenalina.controller.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.io.Serializable;
import java.lang.invoke.WrongMethodTypeException;
import java.util.List;

public abstract class BoardView extends Observable implements Observer, BoardViewInterface {

  private static final long serialVersionUID = 2545732483334205102L;
  private Board board;
  private final Timer timer = new Timer();

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public void setBoard(Board board) {
    this.board = board;
  }

  @Override
  public void startTimer(int time) {
    timer.start(time);
  }

  @Override
  public void hideTimer() {
    timer.stop();
  }

  @Override
  public abstract void showMessage(MessageSeverity severity, String title, String message);

  @Override
  public abstract void showTargetSelect(TargetType type, List<Target> targets);

  @Override
  public abstract void showDirectionSelect();

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
  public void update(TimerSetEvent event) {
    if (event.getTimer() == 0) {
      hideTimer();
    } else {
      startTimer(event.getTimer());
    }
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }
}