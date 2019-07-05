package it.polimi.se2019.adrenalina.controller.action.weapon;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.PlayerStatus;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.controller.action.game.EndGame;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsException;
import it.polimi.se2019.adrenalina.exceptions.NoTargetsExceptionOptional;
import it.polimi.se2019.adrenalina.model.Board;
import it.polimi.se2019.adrenalina.model.DominationBoard;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Weapon;
import org.junit.Before;
import org.junit.Test;

public class SelectActionTest {
  private SelectAction action1;
  private SelectAction action2;
  private Board board;
  private Weapon weapon1;
  private Player player2;
  private Square square;

  @Before
  public void prepare() {
    action1 = new SelectAction(0, 3, TargetType.ATTACK_TARGET)
        .setMinDistance(1)
        .setMaxDistance(5)
        .setDifferentFrom(1, 2, 3)
        .setBetween(4, 5, 6)
        .setVisible(false)
        .setOptional(true)
        .setUseLastDirection(true)
        .setSkippable(true)
        .setDifferentRoom(false)
        .setStopPropagation(false)
        .setDisallowSpawnPoint(false);
    action2 = new SelectAction(0, 3, TargetType.ATTACK_TARGET)
        .setMinDistance(1)
        .setMaxDistance(5)
        .setDifferentFrom(1, 3, 2)
        .setBetween(4, 5, 6)
        .setUseLastDirection(true)
        .setSkippable(true);
    board = new DominationBoard();
    Player player1 = new Player("test1", PlayerColor.GREEN, board);
    player2 = new Player("test2", PlayerColor.YELLOW, board);
    Player player3 = new Player("test3", PlayerColor.GREY, board);
    Player player4 = new Player("test4", PlayerColor.PURPLE, board);
    Player player5 = new Player("test5", PlayerColor.BLUE, board);
    square = new Square(0, 0, SquareColor.BLUE, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR}, board);
    square.setSpawnPoint(true);
    player1.setSquare(square);
    player2.setSquare(square);
    player3.setSquare(square);
    player4.setSquare(square);
    player5.setSquare(square);
    player1.setStatus(PlayerStatus.PLAYING);
    player2.setStatus(PlayerStatus.PLAYING);
    player3.setStatus(PlayerStatus.PLAYING);
    player4.setStatus(PlayerStatus.PLAYING);
    player5.setStatus(PlayerStatus.PLAYING);
    board.addPlayer(player1);
    board.addPlayer(player2);
    board.addPlayer(player3);
    board.addPlayer(player4);
    board.addPlayer(player5);
    board.setSquare(square);
    board.setSquare(new Square(0, 1, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR}, board));
    weapon1 = new Weapon(0,0,0, AmmoColor.YELLOW, "test", "q");
    weapon1.setTargetHistory(0, player1);
  }

  @Test
  public void testEquals() {
    SelectAction action3 = new SelectAction(0, 3, TargetType.ATTACK_TARGET)
        .setMinDistance(1)
        .setMaxDistance(5)
        .setDifferentFrom(1, 2, 3)
        .setBetween(4, 5, 6)
        .setVisible(false)
        .setOptional(true)
        .setUseLastDirection(true)
        .setSkippable(true);
    assertNotEquals(action1, action2);
    assertNotEquals(action1, new EndGame());
    assertNotEquals(action1, new ShootAction(2, 1, 2, false));
    assertEquals(action1, action3);
    assertEquals(action1.hashCode(), action3.hashCode());
  }

  @Test
  public void testGetters() {
    assertTrue(action1.isSync());
    assertEquals(0, action1.getFrom());
    assertEquals(3, action1.getTarget());
    assertEquals(1, action1.getMinDistance());
    assertEquals(5, action1.getMaxDistance());
    assertArrayEquals(new int[]{1, 2, 3}, action1.getDifferentFrom());
    assertArrayEquals(new int[]{4, 5, 6}, action1.getBetween());
    assertFalse(action1.isVisible());
    assertTrue(action1.isOptional());
    assertTrue(action1.isUseLastDirection());
    assertFalse(action1.isDifferentRoom());
    assertEquals(TargetType.ATTACK_TARGET, action1.getSelectType());
    assertTrue(action1.isSkippable());
    assertFalse(action1.isStopPropagation());
    assertEquals(WeaponActionType.SELECT, action1.getActionType());
  }

  @Test
  public void testSerialization() {
    SelectAction action3 = SelectAction.deserialize(action1.serialize());
    assertEquals(action3, action1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSerializationException() {
    SelectAction.deserialize(null);
  }

  @Test(expected = NoTargetsExceptionOptional.class)
  public void testExecuteNoTargetsAvailableOptional() throws Exception {
    action1.execute(board, weapon1);
  }

  @Test(expected = NoTargetsException.class)
  public void testExecuteNoTargetsAvailable() throws Exception {
    action2.execute(board, weapon1);
  }

  @Test(expected = NoTargetsExceptionOptional.class)
  public void testExecuteOptionalSelectAlreadyUsed() throws Exception {
    weapon1.setTargetHistory(3, player2);
    action1.execute(board, weapon1);
  }

  @Test(expected = NoTargetsExceptionOptional.class)
  public void testExecuteNoPropagationAllowed() throws Exception {
    SelectAction action = new SelectAction(1, 3, TargetType.ATTACK_TARGET)
        .setMinDistance(0)
        .setMaxDistance(10)
        .setSkippable(true)
        .setStopPropagation(true);
    weapon1.setTargetHistory(1, square);
    action.execute(board, weapon1);
  }

  @Test
  public void testExecuteNoExceptions() throws Exception {
    SelectAction action = new SelectAction(1, 3, TargetType.ATTACK_TARGET)
        .setMinDistance(0)
        .setMaxDistance(10)
        .setSkippable(true);
    weapon1.setTargetHistory(1, square);
    action.execute(board, weapon1);
    assertEquals(weapon1.getOwner(), weapon1.getTargetHistory(0));
  }

  @Test
  public void testExecuteNoExceptionsDifferentRoom() throws Exception {
    SelectAction action = new SelectAction(1, 3, TargetType.MOVE_SQUARE)
        .setMinDistance(0)
        .setMaxDistance(10)
        .setDifferentRoom(true)
        .setSkippable(true);
    weapon1.setTargetHistory(1, square);
    action.execute(board, weapon1);
    assertEquals(weapon1.getOwner(), weapon1.getTargetHistory(0));
  }
}

