package it.polimi.se2019.adrenalina.controller.action.weapon;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShootActionTest {
    private Board board;
    private Player player1;
    private Player player2;
    private Player player3;
    private Weapon weapon;
    private ShootAction action;
    private ShootAction action2;

    @Before
    public void set() {
        action = new ShootAction(1,1,0,false);
        action2 = new ShootAction(2,1,0,true);

        board = new Board();
        board.setSquare(new Square(0, 0, SquareColor.RED, new BorderType[]{BorderType.WALL, BorderType.AIR, BorderType.AIR, BorderType.WALL}, board));
        board.setSquare(new Square(0, 1, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.WALL}, board));
        board.setSquare(new Square(0, 2, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.WALL, BorderType.WALL}, board));
        board.setSquare(new Square(1, 0, SquareColor.RED, new BorderType[]{BorderType.WALL, BorderType.AIR, BorderType.AIR, BorderType.AIR}, board));

        board.setSquare(new Square(1, 1, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR}, board));

        board.setSquare(new Square(1, 2, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.WALL, BorderType.AIR, BorderType.AIR}, board));
        board.setSquare(new Square(2, 0, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.WALL, BorderType.WALL}, board));
        board.setSquare(new Square(2, 1, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.WALL, BorderType.AIR}, board));
        board.setSquare(new Square(2, 2, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.WALL, BorderType.WALL, BorderType.AIR}, board));

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
    public void testExecute() {
        player2.addTags(player1.getColor(), 1);
        player3.addTags(player1.getColor(), 1);
        action.execute(board, weapon);
        action2.execute(board, weapon);
        assertEquals(2, player2.getDamages().size());
        assertEquals(1, player3.getDamages().size());
    }

    @Test
    public void testSerialize() {
        String json = action.serialize();
        assertEquals(action, ShootAction.deserialize(json));
        assertNotEquals(action, new ShootAction(0,1,0,false));
        assertNotEquals(action, new ShootAction(1,0,0,false));
        assertNotEquals(action, new ShootAction(1,1,1,false));
        assertNotEquals(action, new ShootAction(1,1,0,true));
        assertEquals(1, ShootAction.deserialize(json).getTarget());
        assertEquals(1, ShootAction.deserialize(json).getDamages());
        assertEquals(0, ShootAction.deserialize(json).getTag());
        assertFalse(ShootAction.deserialize(json).isPowerup());

    }
}