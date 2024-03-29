package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.action.weapon.TargetType;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Target;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Observer;
import it.polimi.se2019.adrenalina.utils.RemoteObservable;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BoardViewInterface extends Observer, RemoteObservable, Serializable {

    default List<EventType> getHandledEvents() throws RemoteException {
      List<EventType> registeredEvents = new ArrayList<>();

      registeredEvents.add(EventType.PLAYER_MASTER_UPDATE);
      registeredEvents.add(EventType.BOARD_STATUS_UPDATE);
      registeredEvents.add(EventType.BOARD_SKULLS_UPDATE);
      registeredEvents.add(EventType.BOARD_HAS_WEAPON_UPDATE);
      registeredEvents.add(EventType.BOARD_KILL_SHOTS_UPDATE);
      registeredEvents.add(EventType.BOARD_ADD_PLAYER_UPDATE);
      registeredEvents.add(EventType.BOARD_REMOVE_PLAYER_UPDATE);
      registeredEvents.add(EventType.BOARD_SET_SQUARE_UPDATE);
      registeredEvents.add(EventType.DOMINATION_BOARD_DAMAGES_UPDATE);
      registeredEvents.add(EventType.SQUARE_AMMO_CARD_UPDATE);
      registeredEvents.add(EventType.SQUARE_WEAPON_UPDATE);
      registeredEvents.add(EventType.MAP_SELECTION_EVENT);
      registeredEvents.add(EventType.PLAYER_COLOR_SELECTION_EVENT);
      registeredEvents.add(EventType.PLAYER_MASTER_UPDATE);

      return registeredEvents;
    }

    Board getBoard() throws RemoteException;

    void sendEvent(Event event) throws RemoteException;

    void setBoard(Board board) throws RemoteException;

    void startTimer(int time) throws RemoteException;

    void hideTimer() throws RemoteException;

    Timer getTimer() throws RemoteException;

    void endLoading(boolean masterPlayer) throws RemoteException;

    void showBoard() throws RemoteException;

    void showTargetSelect(TargetType type, List<Target> targets, boolean skippable) throws RemoteException;

    void showDirectionSelect() throws RemoteException;

    void showSquareSelect(List<Target> targets) throws RemoteException;

    void showBuyableWeapons(List<Weapon> weapons) throws RemoteException;

    void showSpawnPointTrackSelection(Map<AmmoColor, Integer> damages) throws RemoteException;

    void showFinalRanks() throws RemoteException;

    void showDisconnectWarning() throws RemoteException;

    void cancelInput() throws RemoteException;
}
