package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public interface BoardViewInterface extends RemoteObservable, Serializable {

  Board getBoard() throws RemoteException;

  void setBoard(Board board) throws RemoteException;

  void startTimer(int time) throws RemoteException;

  void hideTimer() throws RemoteException;

  void showBoard() throws RemoteException;

  void showTargetSelect(TargetType type, List<Target> targets) throws RemoteException;

  void showDirectionSelect() throws RemoteException;

  void showSquareSelect(List<Target> targets) throws RemoteException;

  void showBuyableWeapons(List<Weapon> weapons) throws RemoteException;

  void showSpawnPointTrackSelection() throws RemoteException;

  void update(SpawnPointDamageEvent event) throws RemoteException;

  void update(TimerSetEvent event) throws RemoteException;

  void update(Event event) throws RemoteException;
}
