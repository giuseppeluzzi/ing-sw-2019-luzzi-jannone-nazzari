package it.polimi.se2019.adrenalina.controller.action.weapon;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Before;
import org.junit.Test;

public class ShootSquareActionTest {

  private Board board;
  private Player player1;
  private Player player2;
  private Player player3;
  private Weapon weapon;
  private ShootSquareAction action;

  @Before
  public void set() {
    action = new ShootSquareAction(0,1,1,1, new int[] {0, 1});
    board = new DominationBoard();
    board.setSquare(new Square(0,0, SquareColor.RED, BorderType.WALL,BorderType.AIR,BorderType.AIR,BorderType.WALL, board));
    board.setSquare(new Square(0,1, SquareColor.RED, BorderType.AIR,BorderType.AIR,BorderType.AIR,BorderType.WALL, board));
    board.setSquare(new Square(0,2, SquareColor.RED, BorderType.AIR,BorderType.AIR,BorderType.WALL,BorderType.WALL, board));
    board.setSquare(new Square(1,0, SquareColor.RED, BorderType.WALL,BorderType.AIR,BorderType.AIR,BorderType.AIR, board));

    board.setSquare(new Square(1,1, SquareColor.RED, BorderType.AIR,BorderType.AIR,BorderType.AIR,BorderType.AIR, board));

    board.setSquare(new Square(1,2, SquareColor.RED, BorderType.AIR,BorderType.WALL,BorderType.AIR,BorderType.AIR, board));
    board.setSquare(new Square(2,0, SquareColor.RED, BorderType.AIR,BorderType.AIR,BorderType.WALL,BorderType.WALL, board));
    board.setSquare(new Square(2,1, SquareColor.RED, BorderType.AIR,BorderType.AIR,BorderType.WALL,BorderType.AIR, board));
    board.setSquare(new Square(2,2, SquareColor.RED, BorderType.AIR,BorderType.WALL,BorderType.WALL,BorderType.AIR, board));

    player1 = new Player("P1", PlayerColor.GREEN, board);
    player2 = new Player("P2", PlayerColor.GREY, board);
    player3 = new Player("P3", PlayerColor.PURPLE, board);
    player1.setSquare(board.getSquare(1,1));
    player2.setSquare(board.getSquare(0,1));
    player3.setSquare(board.getSquare(1,0));
    board.addPlayer(player1);
    board.addPlayer(player2);
    board.addPlayer(player3);
    weapon = new Weapon(0,0,0, AmmoColor.BLUE,"test","f");
    weapon.setTargetHistory(0, player1);
    weapon.setTargetHistory(1, player2);
    weapon.setTargetHistory(2, player3);
  }

  @Test
  public void testShootSquare() {
    assertEquals(player3, action.getPlayers(board, weapon).get(0));
  }

  @Test
  public void testExecute() {
    action.execute(board, weapon);
    assertEquals(1, player3.getDamages().size());
  }

  @Test
  public void testExecuteDomination() {
    player1.getSquare().setSpawnPoint(true);
    action.execute(board, weapon);
    assertEquals(1, ((DominationBoard) board).getRedDamages().size());
  }

  @Test
  public void testSerialize() {
    String json = action.serialize();
    assertArrayEquals(action.getExclude(), ShootSquareAction.deserialize(json).getExclude());
    assertEquals(action.getDistance(), ShootSquareAction.deserialize(json).getDistance());
    assertEquals(action.hashCode(), ShootSquareAction.deserialize(json).hashCode());
    assertNotEquals(action, new ShootRoomAction(1,1,1));
    assertNotEquals(action, new ShootSquareAction(1,1,1,1, new int[] {0, 1}));
    assertNotEquals(action, new ShootSquareAction(0,2,1,1, new int[] {0, 1}));
    assertNotEquals(action, new ShootSquareAction(0,1,2,1, new int[] {0, 1}));
    assertNotEquals(action, new ShootSquareAction(0,1,1,2, new int[] {0, 1}));
    assertNotEquals(action, new ShootSquareAction(0,1,1,1, new int[] {0, 8}));
    assertEquals(action, ShootSquareAction.deserialize(json));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSerializeException() {
    ShootSquareAction.deserialize(null);
  }

}