package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public abstract class BoardView extends Observable implements Observer, BoardViewInterface {

  private static final long serialVersionUID = 2545732483334205102L;
  private final Set<EventType> registeredEvents = new HashSet<>();

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
    if (registeredEvents.contains(event.getEventType())) {
      Log.debug("BoardView", "Event received: " + event.getEventType());
      try {
        getClass().getMethod("update", event.getEventType().getEventClass())
            .invoke(this, event);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        //
      }
    }
  }
}