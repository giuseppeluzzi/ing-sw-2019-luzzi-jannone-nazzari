package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.modelview.CurrentPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerAmmoUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDamagesTagsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerFrenzyUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerKillScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.*;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Player dashboards view.
 */
public abstract class PlayerDashboardsView extends Observable implements
    PlayerDashboardsViewInterface {

  private static final long serialVersionUID = -6150690431150041388L;
  private final BoardView boardView;

  protected PlayerDashboardsView(BoardView boardView) {
    this.boardView = boardView;
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerDamagesTagsUpdate
   */
  public void update(PlayerDamagesTagsUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    List<PlayerColor> newDamages = new ArrayList<>(event.getDamages());
    List<PlayerColor> newTags = new ArrayList<>(event.getTags());
    newDamages.removeAll(player.getDamages());
    newTags.removeAll(player.getTags());

    try {
      String killerName = boardView.getBoard().getPlayerByColor(event.getKillerColor()).getName();
      String playerName = boardView.getBoard().getPlayerByColor(event.getPlayerColor()).getName();

      if (!newDamages.isEmpty()) {
        if (boardView.getClient().getPlayerColor() == event.getPlayerColor()) {
          boardView.getClient().showGameMessage(
              String.format(
                  "Hai subito %d danni da %s%s%s!",
                  newDamages.size(),
                  event.getKillerColor().getAnsiColor(),
                  killerName,
                  ANSIColor.RESET));
          boardView.showBoard();
        } else {
          if (newDamages.size() == 1) {
            boardView.getClient().showGameMessage(
                String.format(
                    "%s%s%s ha inflitto %d dann0 a %s%s%s!",
                    event.getKillerColor().getAnsiColor(),
                    killerName,
                    ANSIColor.RESET,
                    newDamages.size(),
                    event.getPlayerColor().getAnsiColor(),
                    playerName,
                    ANSIColor.RESET));
          } else {
            boardView.getClient().showGameMessage(
                String.format(
                    "%s%s%s ha inflitto %d danni a %s%s%s!",
                    event.getKillerColor().getAnsiColor(),
                    killerName,
                    ANSIColor.RESET,
                    newDamages.size(),
                    event.getPlayerColor().getAnsiColor(),
                    playerName,
                    ANSIColor.RESET));
          }
        }
      }

      if (!newTags.isEmpty()) {
        Log.debug("Numero di marchi: " + newTags.size());
        if (boardView.getClient().getPlayerColor() == event.getPlayerColor()) {
          boardView.getClient().showGameMessage(
              String.format(
                  "Hai ricevuto %d marchi da %s%s%s!",
                  newTags.size(),
                  event.getKillerColor().getAnsiColor(),
                  killerName,
                  ANSIColor.RESET));
          boardView.showBoard();
        } else {
          if (newTags.size() == 1) {
            boardView.getClient().showGameMessage(
                String.format(
                    "%s%s%s ha inflitto %d marchio a %s%s%s!",
                    event.getPlayerColor().getAnsiColor(),
                    killerName,
                    ANSIColor.RESET,
                    newTags.size(),
                    event.getKillerColor().getAnsiColor(),
                    playerName,
                    ANSIColor.RESET));
          } else {
            boardView.getClient().showGameMessage(
                String.format(
                    "%s%s%s ha inflitto %d marchi a %s%s%s!",
                    event.getPlayerColor().getAnsiColor(),
                    killerName,
                    ANSIColor.RESET,
                    newTags.size(),
                    event.getKillerColor().getAnsiColor(),
                    playerName,
                    ANSIColor.RESET));
          }
        }
      }
    } catch (InvalidPlayerException ignored) {
      //
    } catch (RemoteException e) {
      Log.exception(e);
    }

    player.updateDamages(event.getDamages());
    player.updateTags(event.getTags());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerFrenzyUpdate
   */
  public void update(PlayerFrenzyUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.setFrenzy(event.isFrenzy());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerScoreUpdate
   */
  public void update(PlayerScoreUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.setScore(event.getScore());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerKillScoreUpdate
   */
  public void update(PlayerKillScoreUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.setKillScore(event.getKillScore());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerStatusUpdate
   */
  public void update(PlayerStatusUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.setStatus(event.getPlayerStatus());
    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()
        && event.getPlayerStatus() == PlayerStatus.SUSPENDED) {
      try {
        showUnsuspendPrompt();
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  /**
   * Event handing.
   * @param event the received event
   * @see PlayerAmmoUpdate
   */
  public void update(PlayerAmmoUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      boardView.getClient().showGameMessage(
          String.format(
              "Munizioni attuali: %s%d rosse%s, %s%d blu%s, %s%d gialle%s",
              ANSIColor.RED,
              event.getRed(),
              ANSIColor.RESET,
              ANSIColor.BLUE,
              event.getBlue(),
              ANSIColor.RESET,
              ANSIColor.YELLOW,
              event.getYellow(),
              ANSIColor.RESET
          ));
    }

    player.updateAmmo(AmmoColor.BLUE, event.getBlue());
    player.updateAmmo(AmmoColor.RED, event.getRed());
    player.updateAmmo(AmmoColor.YELLOW, event.getYellow());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see OwnWeaponUpdate
   */
  public void update(OwnWeaponUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.updateWeapons(event.getWeapons());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see EnemyWeaponUpdate
   */
  public void update(EnemyWeaponUpdate event) {
    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      // This update is not for me
      // Already filtered by Observable, just another level of security
      return;
    }

    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.updateWeapons(event.getUnloadedWeapons());
    player.setWeaponCount(event.getNumWeapons());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see EnemyPowerUpUpdate
   */
  public void update(EnemyPowerUpUpdate event) {
    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      // This update is not for me
      // Already filtered by Observable, just another level of security
      return;
    }

    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.setPowerUpCount(event.getPowerUpsNum());
  }

  /**
   * Event handing.
   * @param event the received event
   * @see OwnPowerUpUpdate
   */
  public void update(OwnPowerUpUpdate event) {
    Player player;
    try {
      player = boardView.getBoard().getPlayerByColor(event.getPlayerColor());
    } catch (InvalidPlayerException e) {
      return;
    }

    player.updatePowerUps(event.getPowerUps());

    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      String powerUpDesc = event.getPowerUps().stream()
          .map(x -> x.getColor().getAnsiColor() + x.getName() + ANSIColor.RESET).collect(
              Collectors.joining(", "));
      boardView.getClient().showGameMessage("PowerUp attuali: " + powerUpDesc);
    }
  }

  private List<PowerUp> addNewtons(Map<AmmoColor, Integer> powerUpData) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (Map.Entry<AmmoColor, Integer> entrySet : powerUpData.entrySet()) {
      for (int i = 0; i < entrySet.getValue(); i++) {
        powerUps.add(new Newton(entrySet.getKey()));
      }
    }
    return powerUps;
  }

  private List<PowerUp> addTargetingScopes(Map<AmmoColor, Integer> powerUpData) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (Map.Entry<AmmoColor, Integer> entrySet : powerUpData.entrySet()) {
      for (int i = 0; i < entrySet.getValue(); i++) {
        powerUps.add(new TargetingScope(entrySet.getKey()));
      }
    }
    return powerUps;
  }

  private List<PowerUp> addTagbackGranades(Map<AmmoColor, Integer> powerUpData) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (Map.Entry<AmmoColor, Integer> entrySet : powerUpData.entrySet()) {
      for (int i = 0; i < entrySet.getValue(); i++) {
        powerUps.add(new TagbackGrenade(entrySet.getKey()));
      }
    }
    return powerUps;
  }

  private List<PowerUp> addTeleporters(Map<AmmoColor, Integer> powerUpData) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (Map.Entry<AmmoColor, Integer> entrySet : powerUpData.entrySet()) {
      for (int i = 0; i < entrySet.getValue(); i++) {
        powerUps.add(new Teleporter(entrySet.getKey()));
      }
    }
    return powerUps;
  }


  /**
   * Event handing.
   * @param event the received event
   * @see CurrentPlayerUpdate
   */
  public void update(CurrentPlayerUpdate event) {
    if (event.getCurrentPlayerColor() != null) {
      if (boardView.getBoard().getCurrentPlayer() != null) {
        try {
          Player previousPlayer = boardView.getBoard()
              .getPlayerByColor(boardView.getBoard().getCurrentPlayer());
          Player newPlayer = boardView.getBoard()
              .getPlayerByColor(event.getCurrentPlayerColor());

          if (boardView.getBoard().getCurrentPlayer() == boardView.getClient().getPlayerColor()) {
            boardView.getClient().showGameMessage(
                String.format(
                    "Hai terminato il turno e ora è il turno di %s%s%s!",
                    event.getCurrentPlayerColor().getAnsiColor(),
                    newPlayer.getName(),
                    ANSIColor.RESET));
          } else {
            boardView.getClient().showGameMessage(
                String.format(
                    "%s%s%s ha terminato il turno e ora è il turno di %s%s%s!",
                    previousPlayer.getColor().getAnsiColor(),
                    previousPlayer.getName(),
                    ANSIColor.RESET,
                    event.getCurrentPlayerColor().getAnsiColor(),
                    newPlayer.getName(),
                    ANSIColor.RESET));
            boardView.showBoard();
          }
        } catch (InvalidPlayerException ignored) {
          //
        } catch (RemoteException e) {
          Log.exception(e);
        }
      }
      boardView.getBoard().setCurrentPlayer(event.getCurrentPlayerColor());
    }
  }

  /**
   * Generic event handling.
   * @param event the event received.
   */
  @Override
  public void update(Event event) {
    try {
      if (getHandledEvents().contains(event.getEventType())) {
        Log.debug("PlayerDashboardsView", "Event received: " + event.getEventType());
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

  @Override
  public PlayerColor getPrivatePlayerColor() {
    return boardView.getClient().getPlayerColor();
  }
}
