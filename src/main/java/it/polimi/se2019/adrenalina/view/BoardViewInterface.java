package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface BoardViewInterface extends Observer, RemoteObservable, Serializable {

    default List<EventType> getHandledEvents() {
      List<EventType> registeredEvents = new ArrayList<>();

      registeredEvents.add(EventType.BOARD_STATUS_UPDATE);
      registeredEvents.add(EventType.BOARD_HAS_WEAPON_UPDATE);
      registeredEvents.add(EventType.BOARD_HAS_AMMO_CARDS_UPDATE);
      registeredEvents.add(EventType.BOARD_KILL_SHOTS_UPDATE);
      registeredEvents.add(EventType.BOARD_ADD_PLAYER_UPDATE);
      registeredEvents.add(EventType.BOARD_SET_SQUARE_UPDATE);
      registeredEvents.add(EventType.DOMINATION_BOARD_DAMAGES_UPDATE);
      registeredEvents.add(EventType.SQUARE_AMMO_CARD_UPDATE);
      registeredEvents.add(EventType.SQUARE_WEAPON_UPDATE);

      return registeredEvents;
    }

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
}
