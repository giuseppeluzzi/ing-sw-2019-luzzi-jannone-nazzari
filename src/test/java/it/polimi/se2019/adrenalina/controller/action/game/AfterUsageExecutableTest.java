package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Test;

public class AfterUsageExecutableTest {

  @Test
  public void testExecute() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, board);
    Weapon weapon = new Weapon(1,0,0, AmmoColor.BLUE,"test","f");
    player.addWeapon(weapon);
    board.addPlayer(player);
    AfterUsageExecutable afterUsageExecutable = new AfterUsageExecutable(null, player, weapon);
    afterUsageExecutable.execute(board);
    assertFalse(weapon.isLoaded());
  }
}