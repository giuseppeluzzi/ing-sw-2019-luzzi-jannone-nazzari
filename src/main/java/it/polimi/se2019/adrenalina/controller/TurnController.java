package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.ActionSelection;
import it.polimi.se2019.adrenalina.controller.action.game.CheckReloadWeapons;
import it.polimi.se2019.adrenalina.controller.action.game.CheckRespawn;
import it.polimi.se2019.adrenalina.controller.action.game.EndGame;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.PickPowerUp;
import it.polimi.se2019.adrenalina.controller.action.game.PowerUpSelection;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import it.polimi.se2019.adrenalina.network.ClientInterface;
import it.polimi.se2019.adrenalina.utils.Log;
import it.polimi.se2019.adrenalina.utils.Timer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Controller in charge of handling a player's turn.
 */
public class TurnController implements Serializable {

  private static final long serialVersionUID = -2990384474014352897L;
  private static final String PLAYER_NOT_EXIST_MSG = "Player doesn't exists anymore!";
  private final BoardController boardController;
  private final transient Deque<GameAction> turnActionsQueue = new ArrayDeque<>();
  private final Timer timer = new Timer();
  private boolean endGame;
  private boolean suspendPlayer;
  private int endGameReason;

  public TurnController(BoardController boardController) {
    this.boardController = boardController;
  }

  public BoardController getBoardController() {
    return boardController;
  }

  /**
   * Getter for endGameReason
   * if 0 then the number of players is below the minimun allowed
   * if 1 then all the skulls were removed and FinalFrenzy wasn't selected
   * if 2 then FinalFrenzy has ended
   * @return endGameReason
   */
  public int getEndGameReason() {
    return endGameReason;
  }

  /**
   * Prepares the turn controller by setting the current player from the board.
   */
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
        Log.critical(PLAYER_NOT_EXIST_MSG);
        return;
      }
    }
    refillMap();
    addGameTurn(currentPlayer);
  }

  /**
   * Executes the next game action in the game action queue with its own timer and suspends players
   * who time out for the set amount of times.
   */
  public void executeGameActionQueue() {
    timer.stop();
    Log.debug("Executing GameAction, total: " + turnActionsQueue.size());
    GameAction gameAction = null;

    while (!turnActionsQueue.isEmpty()) {
      gameAction = turnActionsQueue.pop();
      Log.debug("GameAction: " + gameAction.getClass().getSimpleName());

      if (gameAction.isSync()) {
        Log.debug("Sync action, waiting for user input");
        break;
      }
      gameAction.execute(boardController.getBoard());
      gameAction = null;
    }

    if (gameAction != null) {
      final GameAction gameAction1 = gameAction;
      timer.start(ServerConfig.getInstance().getTurnTimeout(), () -> {
        gameAction1.handleTimeout();
        turnActionsQueue.clear();
        gameAction1.getPlayer().incrementTimeoutCount();
        if (gameAction1.getPlayer().getTimeoutCount() >= ServerConfig.getInstance().getSuspendTimeoutCount()) {
          suspendPlayer = true;
        }
        executeGameActionQueue();
      });
      gameAction.execute(boardController.getBoard());
    } else if (! endGame) {
      endTurn();
    }
  }

  /**
   * Adds a list of game actions to the actions queue.
   * @param gameActions the list of game actions
   */
  public void addTurnActions(List<GameAction> gameActions) {
    List<GameAction> reversedGameActions = new ArrayList<>(gameActions);
    Collections.reverse(reversedGameActions);

    for (GameAction gameAction : reversedGameActions) {
      turnActionsQueue.addFirst(gameAction);
    }
  }

  /**
   * @see #addTurnActions(List)
   */
  public void addTurnActions(GameAction... gameActions) {
    addTurnActions(Arrays.asList(gameActions));
  }

  /**
   * Clears the actions queue
   */
  public void clearActionsQueue() {
    turnActionsQueue.clear();
  }

  /**
   * Finalizes the current turn and prepares for the next one.
   */
  public void endTurn() {
    Player currentPlayer;

    try {
      currentPlayer = boardController.getBoard()
          .getPlayerByColor(boardController.getBoard().getCurrentPlayer());
    } catch (InvalidPlayerException e) {
      Log.critical(PLAYER_NOT_EXIST_MSG);
      return;
    }

    currentPlayer.setCurrentBuying(null);
    currentPlayer.setCurrentExecutable(null);

    for (Weapon weapon : currentPlayer.getWeapons()) {
      weapon.reset();
    }

    Log.debug(currentPlayer.getName() + " ended his turn");
    int currentPlayerIndex = boardController.getBoard().getPlayers().indexOf(currentPlayer);

    if (boardController.getBoard().getActivePlayers().size() < ServerConfig.getInstance().getMinNumPlayers()) {
      showEndGameReason("Il numero di giocatori è sceso sotto al limite minimo!");
      endGameReason = 0;
      endGame = true;
      turnActionsQueue.add(new EndGame());
      executeGameActionQueue();
      return;
    } else if (boardController.getBoard().getSkulls() == 0 && !boardController.getBoard().isFinalFrenzySelected()) {
      showEndGameReason("Tutti i teschi del tracciato mortale sono stati presi!");
      endGameReason = 1;
      endGame = true;
      turnActionsQueue.add(new EndGame());
      executeGameActionQueue();
      return;
    } else if (boardController.getBoard().isFinalFrenzyActive() && currentPlayerIndex == getFfActivatorIndex()) {
      showEndGameReason("La frenesia finale è terminata!");
      endGameReason = 2;
      endGame = true;
      turnActionsQueue.add(new EndGame());
      executeGameActionQueue();
      return;
    }

    if (suspendPlayer) {
      currentPlayer.setStatus(PlayerStatus.SUSPENDED);
      suspendPlayer = false;
    }

    boolean next = true;

    while (next) {
      if (currentPlayerIndex + 1 == boardController.getBoard().getPlayers().size()) {
        currentPlayerIndex = 0;
        boardController.getBoard().incrementTurnCounter();
      } else {
        currentPlayerIndex++;
      }
      currentPlayer = boardController.getBoard().getPlayers().get(currentPlayerIndex);
      next = currentPlayer.getStatus() == PlayerStatus.SUSPENDED || currentPlayer.getStatus() == PlayerStatus.DISCONNECTED;
    }

    if (boardController.getBoard().getStatus() == BoardStatus.FINAL_FRENZY_ENABLED) {
      boardController.getBoard().setStatus(BoardStatus.FINAL_FRENZY);
    }

    refillMap();
    addGameTurn(currentPlayer);
    boardController.getBoard().setCurrentPlayer(currentPlayer.getColor());
    executeGameActionQueue();
  }

  /**
   * Shows a message on all clients explaining the reason why the game has ended.
   * @param message the message to show
   */
  private void showEndGameReason(String message) {
    for (ClientInterface client : boardController.getClients().keySet()) {
      try {
        client.showGameMessage(message);
      } catch (RemoteException e) {
        Log.exception(e);
      }
    }
  }

  /**
   * Adds a first spawn action to the queue.
   * @param player the playing player
   */
  private void addFirstSpawn(Player player) {
    player.setStatus(PlayerStatus.PLAYING);
    addTurnActions(new PickPowerUp(player), new PickPowerUp(player),
        new PowerUpSelection(this, player, null, true, false));
  }

  /**
   * Adds a respawn action to the queue.
   * @param player the playing player
   */
  public void addRespawn(Player player) {
    addTurnActions(new PickPowerUp(player), new PowerUpSelection(this, player, null, true, false));
  }

  /**
   * Adds a game turn for a player.
   * @param player the player for which to add a game turn
   */
  private void addGameTurn(Player player) {
    if (boardController.getBoard().getTurnCounter() == 1) {
      player.addAmmo(AmmoColor.BLUE, 1);
      player.addAmmo(AmmoColor.RED, 1);
      player.addAmmo(AmmoColor.YELLOW, 1);
      addBaseGameTurnActions(player);
      addFirstSpawn(player);
    } else {
      if (boardController.getBoard().isFinalFrenzySelected() &&
          boardController.getBoard().isFinalFrenzyActive()) {

        if (! player.isFrenzy()) {
          player.enableFrenzy();
        }

        for (Player player2 : boardController.getBoard().getPlayers()) {
          Log.debug("Index of " + player2.getName() + ": " + boardController.getBoard().getPlayers().indexOf(player2));
        }
        Log.debug("Index of " + boardController.getBoard().getFinalFrenzyActivator() + "(ff activator): " + getFfActivatorIndex());
        addFinalFrenyTurnActions(player);
      } else {
        addBaseGameTurnActions(player);
      }
    }
  }

  /**
   * Returns size of the actionQueue, used for testing
   * @return int
   */
  int getActionQueueSize() {
    return turnActionsQueue.size();
  }

  /**
   * Disables all game actions that can be disabled (ExecutableEffect actions)
   */
  public void disableActionsUntilPowerup() {
    for (GameAction action : turnActionsQueue) {
      action.setEnabled(false);
    }
  }

  /**
   * Returns the index of the player who activated Final Frenzy.
   * @return the index of the player who activated Final Frenzy
   */
  private Integer getFfActivatorIndex() {
    PlayerColor ffActivatorColor = boardController.getBoard().getFinalFrenzyActivator();
    Player ffActivator;
    try {
      ffActivator = boardController.getBoard().getPlayerByColor(ffActivatorColor);
    } catch (InvalidPlayerException e) {
      Log.critical(e.toString());
      return null;
    }
    return boardController.getBoard().getPlayers().indexOf(ffActivator);
  }

  /**
   * Adds base game actions common to any turn.
   * @param player the playing player
   */
  private void addBaseGameTurnActions(Player player) {
    addTurnActions(
        new PowerUpSelection(this, player, null, false, false),
        new ActionSelection(this, player),
        new PowerUpSelection(this, player, null, false, false),
        new ActionSelection(this, player),
        new PowerUpSelection(this, player, null, false, false),
        new CheckReloadWeapons(this, player),
        new CheckRespawn(this, player));
  }

  /**
   * Adds final frenzy game action.
   * @param player the considered player
   */
  private void addFinalFrenyTurnActions(Player player) {
    int playerIndex = boardController.getBoard().getPlayers().indexOf(player);
    if (playerIndex > getFfActivatorIndex()) {
      addTurnActions(
              new PowerUpSelection(this, player, null, false, false),
              new ActionSelection(this, player),
              new PowerUpSelection(this, player, null, false, false),
              new ActionSelection(this, player),
              new PowerUpSelection(this, player, null, false, false),
              new CheckRespawn(this, player));
    } else {
      addTurnActions(
              new PowerUpSelection(this, player, null, false, false),
              new ActionSelection(this, player),
              new PowerUpSelection(this, player, null, false, false),
              new CheckRespawn(this, player));
    }
  }

  /**
   * Getter that specifies if it's the last turn
   * @return endGame
   */
  public boolean isEndGame() {
    return endGame;
  }

  /**
   * Refills a map with new weapons and powerUps to replace the taken ones.
   */
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
