package it.polimi.se2019.adrenalina.controller.action.game;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Test;

public class ObjectPickupTest {

  @Test
  public void testGetWeapons() {
    Player player = new Player("test", PlayerColor.GREEN, null);
    Square square = new Square(0, 0, SquareColor.RED, WALL, WALL, WALL, WALL, null);
    Weapon weapon = new Weapon(0,0,0, AmmoColor.YELLOW, "test", "e");
    Weapon weapon2 = new Weapon(0,0,2, AmmoColor.YELLOW, "test2", "g");
    player.addAmmo(AmmoColor.YELLOW, 1);
    ObjectPickup objectPickup = new ObjectPickup(player);
    square.setSpawnPoint(true);
    square.addWeapon(weapon);
    square.addWeapon(weapon2);
    player.setSquare(square);
    assertEquals(1, objectPickup.getBuyableWeapons().size());
    assertTrue(objectPickup.isSync());
  }

  @Test
  public void testFetchAmmoCard() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREEN, board);
    Square square = new Square(0, 0, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    player.setSquare(square);
    square.setAmmoCard(new AmmoCard(3,0,0,0));
    ObjectPickup objectPickup = new ObjectPickup(player);
    objectPickup.fetchAmmoCard(board);
    assertEquals(3, player.getAmmo(AmmoColor.RED));
    board.addPowerUp(new Newton(AmmoColor.RED));
    square.setAmmoCard(new AmmoCard(0,2,0,1));
    objectPickup.fetchAmmoCard(board);
    assertEquals(AmmoColor.RED, player.getPowerUps().get(0).getColor());
  }
}