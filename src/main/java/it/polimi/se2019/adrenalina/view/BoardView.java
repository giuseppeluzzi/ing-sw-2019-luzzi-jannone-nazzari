package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.event.modelview.*;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.*;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Timer;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

/**
 * Board view.
 */
public abstract class BoardView extends Observable implements BoardViewInterface {

  private static final long serialVersionUID = 2545732483334205102L;

  private final transient Client client;
  private Board board;
  private final Timer timer;

  protected BoardView(Client client, Timer timer) {
    this.client = client;
    this.timer = timer;
    initializeBoard();
  }

  private void initializeBoard() {
    if (client.isDomination()) {
      board = new DominationBoard();
    } else {
      board = new Board();
    }
  }

  public Client getClient() {
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

  public void sendEvent(Event event) throws RemoteException {
    notifyObservers(event);
  }

  /**
   * Event handing.
   * @param event the received event
   * @see TimerSetEvent
   */
  public void update(TimerSetEvent event) {
    if (event.getTimer() == 0) {
      hideTimer();
    } else {
      startTimer(event.getTimer());
    }
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerMasterUpdate
   */
  public void update(PlayerMasterUpdate event) {
    Player player;
    try {
      player = board.getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.setMaster(event.isMaster());

    if (event.getPlayerColor() == client.getPlayerColor()) {
      try {
        endLoading(player.isMaster());
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardStatusUpdate
   */
  public void update(BoardStatusUpdate event) {
    board.setStatus(event.getStatus());
    if (board.getStatus() == BoardStatus.FINAL_FRENZY) {
      client.showGameMessage("È iniziata la frenesia finale!");
    }
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardAddPlayerUpdate
   */
  public void update(BoardAddPlayerUpdate event) {
    board.addPlayer(new Player(event.getPlayerName(), event.getPlayerColor(), board));
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardRemovePlayerUpdate
   */
  public void update(BoardRemovePlayerUpdate event) {
    try {
      board.removePlayer(event.getPlayerColor());
    } catch (InvalidPlayerException ignored) {
      //
    }
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardSetSquareUpdate
   */
  public void update(BoardSetSquareUpdate event) {
    Square square = new Square(event.getPosX(),
        event.getPosY(),
        event.getColor(),
        event.getEdgeUp(),
        event.getEdgeRight(),
        event.getEdgeDown(),
        event.getEdgeLeft(),
        board);
    if (event.isSpawnPoint()) {
      square.setSpawnPoint(true);
    }

    board.setSquare(square);
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardHasWeaponsUpdate
   */
  public void update(BoardHasWeaponsUpdate event) {
    board.setPublicCopyHasWeapons(event.hasWeapons());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardHasAmmoCardsUpdate
   */
  public void update(BoardHasAmmoCardsUpdate event) {
    board.setPublicCopyHasAmmoCards(event.hasAmmoCards());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardKillShotsUpdate
   */
  public void update(BoardKillShotsUpdate event) {
    board.updateKillShots(event.getPlayers());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see BoardSkullsUpdate
   */
  public void update(BoardSkullsUpdate event) {
    board.setSkulls(event.getSkulls());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see DominationBoardDamagesUpdate
   */
  public void update(DominationBoardDamagesUpdate event) {
    ((DominationBoard) board).updateDamages(event.getSpawnPointColor(), event.getPlayers());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see SquareAmmoCardUpdate
   */
  public void update(SquareAmmoCardUpdate event) {
    if (event.getBlue() == 0 && event.getRed() == 0 &&
        event.getYellow() == 0 && event.getPowerUps() == 0) {
      board.getSquare(event.getPosX(), event.getPosY()).setAmmoCard(null);
    }

    board.getSquare(event.getPosX(), event.getPosY()).setAmmoCard(
        new AmmoCard(event.getRed(), event.getBlue(), event.getYellow(), event.getPowerUps()));
  }

  /**
   * Event handing.
   * @param event the received event
   * @see SquareWeaponUpdate
   */
  public void update(SquareWeaponUpdate event) {
    board.getSquare(event.getPosX(), event.getPosY()).setWeapons(event.getWeapons());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see MapSelectionEvent
   */
  public void update(MapSelectionEvent event) {
    board.setMapId(event.getMap());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerColorSelectionEvent
   */
  public void update(PlayerColorSelectionEvent event) {
    try {
      board.getPlayerByColor(event.getPlayerColor()).setColor(event.getNewPlayerColor());
    } catch (InvalidPlayerException ignored) {
      //
    }
  }

  /**
   * Generic weapon handling.
   * @param event the received event.
   */
  @Override
  public void update(Event event) {
    try {
      if (getHandledEvents().contains(event.getEventType())) {
        Log.debug("BoardView", "Event received: " + event.getEventType());
        getClass().getMethod("update", event.getEventType().getEventClass())
            .invoke(this, event);
      }
    } catch (RemoteException
        | NoSuchMethodException
        | InvocationTargetException
        | IllegalAccessException ignored) {
      //
    }
  }
}