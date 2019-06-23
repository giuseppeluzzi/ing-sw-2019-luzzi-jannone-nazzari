package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.se2019.adrenalina.controller.BorderType.WALL;
import static org.junit.Assert.assertEquals;

public class WeaponReloadTest {
  private Weapon weapon;
  private WeaponReload weaponReload;

  @Before
  public void setObjects() {
    weapon = new Weapon(1,2,1, AmmoColor.YELLOW,"test","w");
    weaponReload = new WeaponReload(weapon);
  }

  @Test
  public void testGet() {
    assertEquals(BuyableType.WEAPON_RELOAD ,weaponReload.getBuyableType());
    assertEquals(weapon.getCost(true), weaponReload.getCost());
    assertEquals(weapon.getName() + " (ricarica)", weaponReload.promptMessage());
    assertEquals(2, weaponReload.getCost(AmmoColor.BLUE));
  }

  @Test(expected = NullPointerException.class)
  public void testAfterPayment() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREY, board);
    player.addWeapon(weapon);
    Square square1 = new Square(0, 0, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square square2 = new Square(0, 1, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square square3 = new Square(1, 0, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square square4 = new Square(1, 1, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    square1.setSpawnPoint(true);
    square1.addWeapon(weapon);
    board.setSquare(square1);
    board.setSquare(square2);
    board.setSquare(square3);
    board.setSquare(square4);
    weaponReload.afterPaymentCompleted(null, board, player);
  }

  @Test(expected = NullPointerException.class)
  public void testAfterPayment2() {
    Board board = new Board();
    Player player = new Player("test", PlayerColor.GREY, board);
    Weapon weapon2 = new Weapon(1,2,1, AmmoColor.YELLOW,"test2","w");
    Square square1 = new Square(0, 0, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square square2 = new Square(0, 1, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square square3 = new Square(1, 0, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    Square square4 = new Square(1, 1, SquareColor.RED, WALL, WALL, WALL, WALL, board);
    WeaponBuy weaponBuy2 = new WeaponBuy(weapon2);
    square1.setSpawnPoint(true);
    square1.addWeapon(weapon2);
    board.setSquare(square1);
    board.setSquare(square2);
    board.setSquare(square3);
    board.setSquare(square4);
    weaponBuy2.afterPaymentCompleted(null, board, player);
  }

}