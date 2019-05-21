package it.polimi.se2019.adrenalina.controller;

import it.polimi.se2019.adrenalina.controller.action.game.ActionSelection;
import it.polimi.se2019.adrenalina.controller.action.game.CheckRespawn;
import it.polimi.se2019.adrenalina.controller.action.game.GameAction;
import it.polimi.se2019.adrenalina.controller.action.game.PickPowerUp;
import it.polimi.se2019.adrenalina.controller.action.game.PowerUpSelection;
import it.polimi.se2019.adrenalina.exceptions.InvalidPlayerException;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.utils.Log;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class TurnController {

  private final BoardController boardController;
  private final Deque<GameAction> turnActionsQueue = new ArrayDeque<>();

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
    addGameTurn(currentPlayer);
  }

  public void executeGameActionQueue() {
    while (!turnActionsQueue.isEmpty()) {
      GameAction gameAction = turnActionsQueue.pop();
      gameAction.execute(boardController.getBoard());
      if (gameAction.isSync()) {
        break;
      }
    }
    endTurn();
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
    } catch (InvalidPlayerException e) {
      Log.critical("Player doesn't exists anymore!");
      return;
    }

    Log.debug(currentPlayer.getName() + " ha terminato il turno!");

    int currentPlayerIndex = boardController.getBoard().getPlayers().indexOf(currentPlayer);

    if (currentPlayerIndex + 1 == boardController.getBoard().getPlayers().size()) {
      currentPlayerIndex = 0;
      boardController.getBoard().incrementTurnCounter();
    } else {
      currentPlayerIndex++;
    }

    currentPlayer = boardController.getBoard().getPlayers().get(currentPlayerIndex);
    boardController.getBoard().setCurrentPlayer(currentPlayer.getColor());

    addGameTurn(currentPlayer);
  }

  private void addFirstSpawn(Player player) {
    addTurnActions(new PickPowerUp(player), new PickPowerUp(player), new PowerUpSelection(player));
  }

  private void addSpawn(Player player) {
    addTurnActions(new PickPowerUp(player), new PowerUpSelection(player));
  }

  private void addGameTurn(Player player) {
    if (boardController.getBoard().getTurnCounter() == 1) {
      addTurnActions(new ActionSelection(player), new ActionSelection(player),
          new CheckRespawn(player));
      addFirstSpawn(player);
    } else {
      if (boardController.getBoard().isFinalFrenzySelected() &&
          boardController.getBoard().isFinalFrenzyActive()) {
        // TODO
      } else {
        addTurnActions(new ActionSelection(player), new ActionSelection(player),
              new CheckRespawn(player));
      }
    }
  }
}
