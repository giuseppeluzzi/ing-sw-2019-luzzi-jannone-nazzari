package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.exceptions.InvalidPowerUpException;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import org.junit.Test;

public class PowerUpSelectionTest {

  @Test
  public void testGetPowerups() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, board);
    Player player2 = new Player("blue", PlayerColor.BLUE, board);
    Player player3 = new Player("grey", PlayerColor.GREY, board);
    Player player4 = new Player("yellow", PlayerColor.YELLOW, board);
    player.setStatus(PlayerStatus.PLAYING);
    player2.setStatus(PlayerStatus.PLAYING);
    player3.setStatus(PlayerStatus.PLAYING);
    player4.setStatus(PlayerStatus.PLAYING);
    board.addPlayer(player);
    board.addPlayer(player2);
    board.addPlayer(player3);
    board.addPlayer(player4);
    PowerUpSelection powerUpSelection = new PowerUpSelection(null, player, player2, true, true);
    try {
      player.addPowerUp(new Newton(AmmoColor.RED));
      player.addPowerUp(new TargetingScope(AmmoColor.BLUE));
      player.addPowerUp(new TagbackGrenade(AmmoColor.YELLOW));
    } catch (InvalidPowerUpException ignore) {
      //
    }
    board.setCurrentPlayer(PlayerColor.GREEN);
    assertEquals(AmmoColor.BLUE, powerUpSelection.getValidPowerUps(board, true).get(0).getColor());
    assertEquals(AmmoColor.RED, powerUpSelection.getValidPowerUps(board, false).get(0).getColor());
    board.setCurrentPlayer(PlayerColor.YELLOW);
    assertEquals(AmmoColor.YELLOW, powerUpSelection.getValidPowerUps(board, false).get(0).getColor());
  }
}