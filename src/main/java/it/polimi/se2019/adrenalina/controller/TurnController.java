package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.ActionSelection;
import it.polimi.se2019.adrenalina.controller.action.game.CheckRespawn;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.PickPowerUp;
import it.polimi.se2019.adrenalina.controller.action.game.PowerUpSelection;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class TurnController implements Serializable {

  private static final long serialVersionUID = -2990384474014352897L;
  private final BoardController boardController;
  private final transient Deque<GameAction> turnActionsQueue = new ArrayDeque<>();
  private final Timer timer = new Timer();

  public TurnController(BoardController boardController) {
    this.boardController = boardController;
  }

  public void prepare() {
    Player currentPlayer;
    if (boardController.getBoard().getCurrentPlayer() == null) {
      currentPlayer = boardController.getBoard().getPlayers().get(0);
      boardController.getBoard().setCurrentPlayer(currentPlayer.getColor());
    } else {
      try {
        currentPlayer = boardController.getBoard()
            .getPlayerByColor(boardController.getBoard().getCurrentPlayer());
      } catch (InvalidPlayerException e) {
        Log.critical("Player doesn't exists anymore!");
        return;
      }
    }
    refillMap();
    addGameTurn(currentPlayer);
  }

  public void executeGameActionQueue() {
    timer.stop();
    Log.debug("execute! " + turnActionsQueue.size());
    GameAction gameAction = null;

    while (!turnActionsQueue.isEmpty()) {
      gameAction = turnActionsQueue.pop();
      Log.debug("GA: " + gameAction.getClass().getSimpleName());

      if (gameAction.isSync()) {
        Log.debug("stooooop");
        break;
      }
      gameAction.execute(boardController.getBoard());
      gameAction = null;
    }

    if (gameAction != null) {
      timer.start(Configuration.getInstance().getTurnTimeout(), () -> {
        turnActionsQueue.clear();
        executeGameActionQueue();
      });
      gameAction.execute(boardController.getBoard());
    } else {
      endTurn();
    }
  }

  public void addTurnActions(List<GameAction> gameActions) {
    List<GameAction> reversedGameActions = new ArrayList<>(gameActions);
    Collections.reverse(reversedGameActions);

    for (GameAction gameAction : reversedGameActions) {
      turnActionsQueue.addFirst(gameAction);
    }
  }

  public void addTurnActions(GameAction... gameActions) {
    addTurnActions(Arrays.asList(gameActions));
  }

  private void endTurn() {
    Player currentPlayer;
    try {
      currentPlayer = boardController.getBoard()
          .getPlayerByColor(boardController.getBoard().getCurrentPlayer());
      currentPlayer.setCurrentWeapon(null);
    } catch (InvalidPlayerException e) {
      Log.critical("Player doesn't exists anymore!");
      return;
    }

    currentPlayer.setCurrentBuying(null);
    currentPlayer.setCurrentExecutable(null);

    for (Weapon weapon : currentPlayer.getWeapons()) {
      weapon.reset();
    }

    Log.debug(currentPlayer.getName() + " ha terminato il turno!");

    int currentPlayerIndex = boardController.getBoard().getPlayers().indexOf(currentPlayer);

    if (currentPlayerIndex + 1 == boardController.getBoard().getPlayers().size()) {
      currentPlayerIndex = 0;
      boardController.getBoard().incrementTurnCounter();
    } else {
      currentPlayerIndex++;
    }

    boardController.getBoard().removeDoubleKill();

    currentPlayer = boardController.getBoard().getPlayers().get(currentPlayerIndex);
    boardController.getBoard().setCurrentPlayer(currentPlayer.getColor());

    refillMap();
    addGameTurn(currentPlayer);
    executeGameActionQueue();
  }

  private void addFirstSpawn(Player player) {
    player.setStatus(PlayerStatus.PLAYING);
    addTurnActions(new PickPowerUp(player), new PickPowerUp(player), new PowerUpSelection(this, player, true, false));
  }

  public void addRespawn(Player player) {
    player.setStatus(PlayerStatus.PLAYING);
    addTurnActions(new PickPowerUp(player), new PowerUpSelection(this, player, true, false));
  }

  private void addGameTurn(Player player) {
    if (boardController.getBoard().getTurnCounter() == 1) {
      if (player.getName().equals("PeppeSocket")) {
        // TODO CHEAT SUITE CANCELLARE
        addTurnActions(new CheckRespawn(this, player, true));
      } else {
        player.addAmmo(AmmoColor.BLUE, 1);
        player.addAmmo(AmmoColor.RED, 1);
        player.addAmmo(AmmoColor.YELLOW, 1);

        addBaseGameTurnActions(player);
      }
      addFirstSpawn(player);
    } else {
      if (boardController.getBoard().isFinalFrenzySelected() &&
          boardController.getBoard().isFinalFrenzyActive()) {

        int playerIndex = boardController.getBoard().getPlayers().indexOf(player);
        int finalFrenzyActivator;

        try {
          finalFrenzyActivator = boardController.getBoard().getPlayers()
              .indexOf(
                  boardController.getBoard().getPlayerByColor(
                      boardController.getBoard().getFinalFrenzyActivator()));
        } catch (InvalidPlayerException e) {
          // Shouldn't happen
          Log.critical("Player doesn't exists anymore!");
          return;
        }

        if (playerIndex > finalFrenzyActivator) {
          addTurnActions(
              new PowerUpSelection(this, player, false, false),
              new ActionSelection(this, player),
              new PowerUpSelection(this, player, false, false),
              new CheckRespawn(this, player, true));
        } else {
          addBaseGameTurnActions(player);
        }
      } else {
        addBaseGameTurnActions(player);
      }
    }
  }

  private void addBaseGameTurnActions(Player player) {
    addTurnActions(
        new PowerUpSelection(this, player, false, false),
        new ActionSelection(this, player),
        new PowerUpSelection(this, player, false, false),
        new ActionSelection(this, player),
        new PowerUpSelection(this, player, false, false),
        new CheckRespawn(this, player, true));
  }

  private void refillMap() {
    for (Square square : boardController.getBoard().getSquares()) {
      if (square.isSpawnPoint()) {
        int weapons = square.getWeapons().size();
        for (int i = 0; i < 3 - weapons; i++) {
          Weapon weapon = boardController.getBoard().getWeapons().get(0);
          boardController.getBoard().takeWeapon(weapon);
          square.addWeapon(weapon);
        }
      } else {
        if (square.getAmmoCard() == null) {
          AmmoCard ammoCard = boardController.getBoard().getAmmoCards().get(0);
          boardController.getBoard().drawAmmoCard(ammoCard);
          square.setAmmoCard(ammoCard);
        }
      }
    }
  }
}
