package it.polimi.se2019.adrenalina.controller;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.rmi.RemoteException;
import org.junit.Before;
import org.junit.Test;

public class AttackControllerTest {
  private AttackController attackController;
  private BoardController boardController;
  private Player player;
  private Board board;

  @Before
  public void setAttackController() {
    board = new Board();
    player = new Player("test", PlayerColor.GREEN, board);
    try {
      attackController = new AttackController(null);
      boardController = new BoardController(true);
    } catch (RemoteException ignore) {
      //
    }
  }

  @Test
  public void testGetReloadingWeapons() {
    Weapon weapon1 = new Weapon(0,0,0,AmmoColor.YELLOW, "test", "q");
    Weapon weapon2 = new Weapon(0,0,0,AmmoColor.YELLOW, "test2", "w");
    Weapon weapon3 = new Weapon(0,1 ,0,AmmoColor.YELLOW, "test3", "e");
    player.addWeapon(weapon1);
    player.addWeapon(weapon2);
    player.addWeapon(weapon3);
    player.addAmmo(AmmoColor.YELLOW, 3);
    weapon1.setLoaded(false);
    weapon2.setLoaded(false);
    weapon3.setLoaded(false);
    assertEquals(1, attackController.getReloadableWeapons(player,weapon1).size());
  }
}