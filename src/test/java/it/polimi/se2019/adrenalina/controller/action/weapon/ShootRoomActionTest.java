package it.polimi.se2019.adrenalina.controller.action.weapon;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import it.polimi.se2019.adrenalina.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShootRoomActionTest {

    private Board board;
    private Player player1;
    private Player player2;
    private Player player3;
    private Weapon weapon;
    private ShootRoomAction action;

    @Before
    public void set() {
        action = new ShootRoomAction(1,1,1);
        board = new DominationBoard();
        board.setSquare(new Square(0, 0, SquareColor.YELLOW, new BorderType[]{BorderType.WALL, BorderType.AIR, BorderType.AIR, BorderType.WALL}, board));
        board.setSquare(new Square(0, 1, SquareColor.YELLOW, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.WALL}, board));
        board.setSquare(new Square(0, 2, SquareColor.YELLOW, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.WALL, BorderType.WALL}, board));
        board.setSquare(new Square(1, 0, SquareColor.YELLOW, new BorderType[]{BorderType.WALL, BorderType.AIR, BorderType.AIR, BorderType.AIR}, board));

        board.setSquare(new Square(1, 1, SquareColor.RED, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.AIR, BorderType.AIR}, board));

        board.setSquare(new Square(1, 2, SquareColor.BLUE, new BorderType[]{BorderType.AIR, BorderType.WALL, BorderType.AIR, BorderType.AIR}, board));
        board.setSquare(new Square(2, 0, SquareColor.BLUE, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.WALL, BorderType.WALL}, board));
        board.setSquare(new Square(2, 1, SquareColor.BLUE, new BorderType[]{BorderType.AIR, BorderType.AIR, BorderType.WALL, BorderType.AIR}, board));
        board.setSquare(new Square(2, 2, SquareColor.BLUE, new BorderType[]{BorderType.AIR, BorderType.WALL, BorderType.WALL, BorderType.AIR}, board));

        player1 = new Player("P1", PlayerColor.GREEN, board);
        player2 = new Player("P2", PlayerColor.GREY, board);
        player3 = new Player("P3", PlayerColor.PURPLE, board);
        player1.setSquare(board.getSquare(1,1));
        player2.setSquare(board.getSquare(0,1));
        player3.setSquare(board.getSquare(1,2));
        board.addPlayer(player1);
        board.addPlayer(player2);
        board.addPlayer(player3);
        weapon = new Weapon(0,0,0, AmmoColor.BLUE,"test","f");
        weapon.setTargetHistory(0, player1);
        weapon.setTargetHistory(1, player2);
        weapon.setTargetHistory(2, player3);
        board.getSquare(0,2).setSpawnPoint(true);
    }

    @Test
    public void testExecute() {
        action.execute(board, weapon);
        assertEquals(1, player2.getDamages().size());
        assertEquals(0, player3.getDamages().size());
        assertEquals(1, ((DominationBoard) board).getYellowDamages().size());
    }

    @Test
    public void testSerialization() {
        String json = action.serialize();
        assertEquals(action, ShootRoomAction.deserialize(json));
        assertNotEquals(action, new ShootSquareAction(1,1,1,1,new int[] {}));
        assertNotEquals(action, new ShootRoomAction(0,1,1));
        assertNotEquals(action, new ShootRoomAction(1,0,1));
        assertNotEquals(action, new ShootRoomAction(1,1,0));
    }

}