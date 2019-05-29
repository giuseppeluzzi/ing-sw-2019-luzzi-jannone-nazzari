package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.event.modelview.BoardAddPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasAmmoCardsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasWeaponsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardKillShotsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSetSquareUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.DominationBoardDamagesUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareAmmoCardUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareWeaponUpdate;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public abstract class BoardView extends Observable implements BoardViewInterface {

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

    registeredEvents.add(EventType.BOARD_STATUS_UPDATE);
    registeredEvents.add(EventType.BOARD_HAS_WEAPON_UPDATE);
    registeredEvents.add(EventType.BOARD_HAS_AMMO_CARDS_UPDATE);
    registeredEvents.add(EventType.BOARD_KILL_SHOTS_UPDATE);
    registeredEvents.add(EventType.BOARD_ADD_PLAYER_UPDATE);
    registeredEvents.add(EventType.BOARD_SET_SQUARE_UPDATE);
    registeredEvents.add(EventType.DOMINATION_BOARD_DAMAGES_UPDATE);
    registeredEvents.add(EventType.SQUARE_AMMO_CARD_UPDATE);
    registeredEvents.add(EventType.SQUARE_WEAPON_UPDATE);
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
  public void update(TimerSetEvent event) {
    if (event.getTimer() == 0) {
      hideTimer();
    } else {
      startTimer(event.getTimer());
    }
  }

  @Override
  public void update(BoardStatusUpdate event) {
    board.setStatus(event.getStatus());
  }

  @Override
  public void update(BoardAddPlayerUpdate event) throws RemoteException {
    board.addPlayer(new Player(event.getPlayerName(), event.getPlayerColor(), board));

  }

  @Override
  public void update(BoardSetSquareUpdate event) {
    board.setSquare(event.getSquare());
  }

  @Override
  public void update(BoardHasWeaponsUpdate event) {
    board.setPublicCopyHasWeapons(event.hasWeapons());
  }

  @Override
  public void update(BoardHasAmmoCardsUpdate event) {
    board.setPublicCopyHasAmmoCards(event.hasAmmoCards());
  }

  @Override
  public void update(BoardKillShotsUpdate event) {
    board.updateKillShots(event.getPlayers());
  }

  @Override
  public void update(DominationBoardDamagesUpdate event) {
    ((DominationBoard) board).updateDamages(event.getSpawnPointColor(), event.getPlayers());
  }

  @Override
  public void update(SquareAmmoCardUpdate event) {
    board.getSquare(event.getPosX(), event.getPosY()).setAmmoCard(
        new AmmoCard(event.getRed(), event.getBlue(), event.getYellow(), event.getPowerUps()));
  }

  @Override
  public void update(SquareWeaponUpdate event) {
    board.getSquare(event.getPosX(), event.getPosY()).setWeapons(event.getWeapons());
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