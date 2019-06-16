package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.action.game.TurnAction;
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
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import it.polimi.se2019.adrenalina.model.Teleporter;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.ANSIColor;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Observable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class PlayerDashboardsView extends Observable implements
    PlayerDashboardsViewInterface {

  private static final long serialVersionUID = -6150690431150041388L;
  private final BoardView boardView;

  protected PlayerDashboardsView(BoardView boardView) {
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
  public abstract void showPaymentOption(BuyableType buyableType,
      Map<AmmoColor, Integer> buyableCost, List<PowerUp> budgetPowerUp,
      Map<AmmoColor, Integer> budgetAmmo);

  @Override
  public abstract void showTurnActionSelection(List<TurnAction> actions);

  @Override
  public abstract void showWeaponSelection(List<Weapon> weapons);

  @Override
  public abstract void showEffectSelection(Weapon weapon, List<Effect> effects);

  @Override
  public abstract void showUnsuspendPrompt();

  @Override
  public abstract void showPowerUpSelection(List<PowerUp> powerUps, boolean discard);

  public void update(PlayerDamagesTagsUpdate event) {
    Player player = getPlayerByColor(event.getPlayerColor());

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

      if (!newTags.isEmpty()) {
        if (boardView.getClient().getPlayerColor() == event.getPlayerColor()) {
          boardView.getClient().showGameMessage(
              String.format(
                  "Hai ricevuto %d marchi da %s%s%s!",
                  newDamages.size(),
                  event.getKillerColor().getAnsiColor(),
                  killerName,
                  ANSIColor.RESET));
        } else {
          boardView.getClient().showGameMessage(
              String.format(
                  "%s%s%s ha inflitto %d marchi a %s%s%s!",
                  event.getPlayerColor().getAnsiColor(),
                  playerName,
                  ANSIColor.RESET,
                  newDamages.size(),
                  event.getKillerColor().getAnsiColor(),
                  killerName,
                  ANSIColor.RESET));
        }
      }
    } catch (InvalidPlayerException ignored) {
      //
    }

    player.updateDamages(event.getDamages());
    player.updateTags(event.getTags());
  }

  public void update(PlayerFrenzyUpdate event) {
    getPlayerByColor(event.getPlayerColor()).setFrenzy(event.isFrenzy());
  }

  public void update(PlayerScoreUpdate event) {
    getPlayerByColor(event.getPlayerColor()).setScore(event.getScore());
  }

  public void update(PlayerKillScoreUpdate event) {
    getPlayerByColor(event.getPlayerColor()).setKillScore(event.getKillScore());
  }

  public void update(PlayerStatusUpdate event) {
    getPlayerByColor(event.getPlayerColor()).setStatus(event.getPlayerStatus());
    if ((event.getPlayerColor() == boardView.getClient().getPlayerColor()) && (event.getPlayerStatus() == PlayerStatus.SUSPENDED)) {
      showUnsuspendPrompt();
    }
  }

  public void update(PlayerAmmoUpdate event) {
    Player player = getPlayerByColor(event.getPlayerColor());
    if (player == null) {
      return ;
    }

    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      boardView.getClient().showGameMessage(
          String.format(
            "Munizioni attuali: %s%d (%d) rosse%s, %s%d (%d) blu%s, %s%d (%d) gialle%s",
              ANSIColor.RED,
              event.getRed(),
              (player.getAmmo(AmmoColor.RED) + event.getRed()) % 3,
              ANSIColor.RESET,
              ANSIColor.BLUE,
              event.getBlue(),
              (player.getAmmo(AmmoColor.BLUE) + event.getBlue()) % 3,
              ANSIColor.RESET,
              ANSIColor.YELLOW,
              event.getYellow(),
              (player.getAmmo(AmmoColor.YELLOW) + event.getYellow()) % 3,
              ANSIColor.RESET
      ));
    }

    player.updateAmmo(AmmoColor.BLUE, event.getBlue());
    player.updateAmmo(AmmoColor.RED, event.getRed());
    player.updateAmmo(AmmoColor.YELLOW, event.getYellow());
  }

  public void update(OwnWeaponUpdate event) {
    getPlayerByColor(event.getPlayerColor()).updateWeapons(event.getWeapons());
  }

  public void update(EnemyWeaponUpdate event) {
    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      // This update is not for me
      return;
    }
    Player player = getPlayerByColor(event.getPlayerColor());
    player.updateWeapons(event.getUnloadedWeapons());
    player.setWeaponCount(event.getNumWeapons());
  }

  public void update(EnemyPowerUpUpdate event) {
    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      // This update is not for me
      return;
    }
    getPlayerByColor(event.getPlayerColor()).setPowerUpCount(event.getPowerUpsNum());
  }

  public void update(OwnPowerUpUpdate event) {
    List<PowerUp> powerUps = new ArrayList<>();
    for (Map.Entry<PowerUpType, Map<AmmoColor, Integer>> entrySet : event.getPowerUps()
        .entrySet()) {
      if (entrySet.getKey() == PowerUpType.NEWTON) {
        powerUps.addAll(addNewtons(entrySet.getValue()));
      } else if (entrySet.getKey() == PowerUpType.TELEPORTER) {
        powerUps.addAll(addTeleporters(entrySet.getValue()));
      } else if (entrySet.getKey() == PowerUpType.TAGBACK_GRANADE) {
        powerUps.addAll(addTagbackGranades(entrySet.getValue()));
      } else if (entrySet.getKey() == PowerUpType.TARGETING_SCOPE) {
        powerUps.addAll(addTargetingScopes(entrySet.getValue()));
      }
    }
    getPlayerByColor(event.getPlayerColor()).updatePowerUps(powerUps);

    if (event.getPlayerColor() == boardView.getClient().getPlayerColor()) {
      String powerUpDesc = powerUps.stream().map(x -> x.getColor().getAnsiColor() + x.getName() + ANSIColor.RESET).collect(
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


  public void update(CurrentPlayerUpdate event) {
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
      }
    } catch (InvalidPlayerException ignored) {
      //
    }
    boardView.getBoard().setCurrentPlayer(event.getCurrentPlayerColor());
  }

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
