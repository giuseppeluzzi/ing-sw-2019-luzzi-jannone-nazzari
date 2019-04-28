package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.controller.event.DoubleKillEvent;
import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.KillShotEvent;
import it.polimi.se2019.adrenalina.controller.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.controller.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;

public class BoardView extends Observable implements Observer, BoardViewInterface {

  private Board board;
  private int timer;
  private final Object timerLock;
  private Thread timerThread;

  public BoardView() {
    timer = 0;
    timerLock = new Object();
    synchronized (timerLock) {
      timerThread = null;
    }
  }

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
    synchronized (timerLock) {
      if (timer != 0) {
        timerThread.interrupt();
        timerThread = null;
      }
      timer = time;

      timerThread = new Thread(() -> {
        while (true) {
          synchronized (timerLock) {
            if (timer <= 0) {
              break;
            }
            Log.info("Timer", "" + timer);
            timer--;
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              timerThread.interrupt();
            }
          }
        }
      });
      timerThread.start();
    }
  }

  @Override
  public void hideTimer() {
    synchronized (timerLock) {
      timerThread.interrupt();
      timer = 0;
    }
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