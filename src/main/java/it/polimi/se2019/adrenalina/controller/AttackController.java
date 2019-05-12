package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerAttackEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.utils.Observer;
import java.lang.invoke.WrongMethodTypeException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AttackController extends UnicastRemoteObject implements Observer {
  private static final long serialVersionUID = -5414473871887210992L;

  private final BoardController boardController;

  public AttackController(BoardController boardController) throws RemoteException {
    this.boardController = boardController;
  }

  public void update(PlayerAttackEvent event) {
    // TODO: invoked when a player attacks a target with a weapon-effect; if the target is a spawnpoint, the domination board should be notified
    // TODO: get real player from the copy
  }

  public void update(PlayerReloadEvent event) {
    // TODO: invoked when a player reloads a weapon
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof AttackController &&
        ((AttackController) obj).boardController.equals(boardController);
  }

  @Override
  public int hashCode() {
    return boardController.hashCode();
  }
}
