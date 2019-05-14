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
import it.polimi.se2019.adrenalina.network.VirtualClientSocket;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.util.List;

public class VirtualBoardView extends Observable implements BoardViewInterface, Observer {
  private Board board;
  private final VirtualClientSocket clientSocket;

  public VirtualBoardView(VirtualClientSocket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public void setBoard(Board board) {
    if (this.board != null) {
      board.removeObserver(this);
    }
    this.board = board;
    if (this.board != null) {
      board.addObserver(this);
    }
  }

  @Override
  public void startTimer(int time) {
    clientSocket.sendEvent(new TimerSetEvent(time));
  }

  @Override
  public void hideTimer() {
    startTimer(0);
  }

  @Override
  public void showMessage(MessageSeverity severity, String title, String message) {
    // TODO: show a message
  }

  @Override
  public void showTargetSelect(TargetType type, List<Target> targets) {
    // TODO: send selection
  }

  @Override
  public void showDirectionSelect() {
    // TODO: send selection
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
