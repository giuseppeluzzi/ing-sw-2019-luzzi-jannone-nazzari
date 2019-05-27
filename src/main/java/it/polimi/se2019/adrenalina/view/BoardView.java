package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.event.AmmoCardUpdateEvent;
import it.polimi.se2019.adrenalina.event.DoubleKillEvent;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.KillShotEvent;
import it.polimi.se2019.adrenalina.event.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.event.WeaponUpdateEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;

public abstract class BoardView extends Observable implements Observer, BoardViewInterface {

  private static final long serialVersionUID = 2545732483334205102L;
  private final transient ClientInterface client;

  private Board board;
  private final Timer timer = new Timer();

  protected BoardView(ClientInterface client) {
    this.client = client;
    try {
      initializeBoard();
    } catch (RemoteException e) {
      Log.exception(e);
    }
  }

  private void initializeBoard() throws RemoteException {
    if (client.isDomination()) {
      board = new DominationBoard();
    } else {
      board = new Board();
    }
  }

  public ClientInterface getClient() {
    return client;
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
    timer.start(time);
  }

  @Override
  public void hideTimer() {
    timer.stop();
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