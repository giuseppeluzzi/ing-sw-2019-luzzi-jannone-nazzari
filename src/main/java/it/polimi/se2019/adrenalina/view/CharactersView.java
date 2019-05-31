package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.MessageSeverity;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDeathUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerPositionUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

public abstract class CharactersView extends Observable implements CharactersViewInterface {

  private static final long serialVersionUID = 3820277997554969634L;

  private final BoardView boardView;

  protected CharactersView(BoardView boardView) {
    this.boardView = boardView;
  }

  public Player getPlayerByColor(PlayerColor playerColor) {
    for (Player player : boardView.getBoard().getPlayers()) {
      if (player.getColor() == playerColor) {
        return player;
      }
    }
    return null;
  }

  @Override
  public void setSelected(PlayerColor playerColor) {
    // TODO: Show selected player
  }

  @Override
  public void removeSelected() {
    // TODO: Remove selected player
  }

  @Override
  public abstract void showDeath(PlayerColor playerColor);

  public void update(PlayerPositionUpdate event) {
    Square newSquare = boardView.getBoard().getSquare(event.getPosX(), event.getPosY());
    try {
      boardView.getBoard().getPlayerByColor(event.getPlayerColor()).setSquare(newSquare);
    } catch (InvalidPlayerException e) {
      Log.critical("Player not found!");
    }
  }

  public void update(PlayerStatusUpdate event) {
    try {
      boardView.getBoard().getPlayerByColor(event.getPlayerColor())
          .setStatus(event.getPlayerStatus());
    } catch (InvalidPlayerException e) {
      Log.critical("Player not found!");
    }
  }

  public void update(PlayerDeathUpdate event) {
    try {
      String killerName = boardView.getBoard().getPlayerByColor(event.getKillerColor()).getName();
      String playerName = boardView.getBoard().getPlayerByColor(event.getPlayerColor()).getName();
      if (boardView.getClient().getPlayerColor() == event.getPlayerColor()) {
        boardView.getClient().showGameMessage(
            String.format(
                "Sei stato ucciso da %s%s%s!",
                event.getKillerColor().getAnsiColor(),
                killerName,
                ANSIColor.RESET));
      } else {
        boardView.getClient().showGameMessage(
            String.format(
                "%s%s%s è stato ucciso da %s%s%s!",
                event.getPlayerColor().getAnsiColor(),
                playerName,
                ANSIColor.RESET,
                event.getKillerColor().getAnsiColor(),
                killerName,
                ANSIColor.RESET));
      }
    } catch (InvalidPlayerException | RemoteException ignored) {
      //
    }
    showDeath(event.getPlayerColor());
  }

  @Override
  public void update(Event event) {
    try {
      if (getHandledEvents().contains(event.getEventType())) {
        Log.debug("CharactersView", "Event received: " + event.getEventType());
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
