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
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.rmi.RemoteException;
import java.util.List;

public interface BoardViewInterface extends RemoteObservable {
  Board getBoard() throws RemoteException;
  void setBoard(Board board) throws RemoteException;

  void startTimer(int time) throws RemoteException;
  void hideTimer() throws RemoteException;

  void showMessage(MessageSeverity severity, String title, String message) throws RemoteException;
  void showTargetSelect(TargetType type, List<Target> targets);
  void showDirectionSelect();

  /*void update(WeaponUpdateEvent event) throws RemoteException;
  void update(AmmoCardUpdateEvent event) throws RemoteException;
  void update(KillShotEvent event) throws RemoteException;
  void update(DoubleKillEvent event) throws RemoteException;
  void update(SpawnPointDamageEvent event) throws RemoteException;

  void update(TimerSetEvent event) throws RemoteException;*/

  void update(Event event) throws RemoteException;
}
