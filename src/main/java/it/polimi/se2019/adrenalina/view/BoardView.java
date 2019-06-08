package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.event.modelview.BoardAddPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasAmmoCardsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasWeaponsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardKillShotsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSetSquareUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardSkullsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.DominationBoardDamagesUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareAmmoCardUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareWeaponUpdate;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.network.Client;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public abstract class BoardView extends Observable implements BoardViewInterface {

  private static final long serialVersionUID = 2545732483334205102L;

  private final transient Client client;
  private Board board;
  private final Timer timer;

  protected BoardView(Client client, Timer timer) {
    this.client = client;
    this.timer = timer;
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

  public void update(TimerSetEvent event) {
    if (event.getTimer() == 0) {
      hideTimer();
    } else {
      startTimer(event.getTimer());
    }
  }

  public void update(BoardStatusUpdate event) {
    board.setStatus(event.getStatus());
    client.showGameMessage("E' iniziata la frenesia finale!");
  }

  public void update(BoardAddPlayerUpdate event) {
    board.addPlayer(new Player(event.getPlayerName(), event.getPlayerColor(), board));
  }

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

  public void update(BoardHasWeaponsUpdate event) {
    board.setPublicCopyHasWeapons(event.hasWeapons());
  }

  public void update(BoardHasAmmoCardsUpdate event) {
    board.setPublicCopyHasAmmoCards(event.hasAmmoCards());
  }

  public void update(BoardKillShotsUpdate event) {
    board.updateKillShots(event.getPlayers());
  }

  public void update(BoardSkullsUpdate event) {
    board.setSkulls(event.getSkulls());
  }

  public void update(DominationBoardDamagesUpdate event) {
    ((DominationBoard) board).updateDamages(event.getSpawnPointColor(), event.getPlayers());
  }

  public void update(SquareAmmoCardUpdate event) {
    if (event.getBlue() == 0 && event.getRed() == 0 &&
        event.getYellow() == 0 && event.getPowerUps() == 0) {
      board.getSquare(event.getPosX(), event.getPosY()).setAmmoCard(null);
    }

    board.getSquare(event.getPosX(), event.getPosY()).setAmmoCard(
        new AmmoCard(event.getRed(), event.getBlue(), event.getYellow(), event.getPowerUps()));
  }

  public void update(SquareWeaponUpdate event) {
    board.getSquare(event.getPosX(), event.getPosY()).setWeapons(event.getWeapons());
  }

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