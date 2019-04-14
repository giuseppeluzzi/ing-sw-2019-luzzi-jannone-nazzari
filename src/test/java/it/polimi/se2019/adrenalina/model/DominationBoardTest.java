package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DominationBoardTest {
  // TODO: complete test suite
  @Test
  public void testSerialization(){
    DominationBoard dominationBoard = new DominationBoard();
    DominationBoard dominationBoard2;
    String json;

    dominationBoard.addBlueDamage(PlayerColor.PURPLE);
    dominationBoard.addBlueDamage(PlayerColor.GREEN);
    dominationBoard.addRedDamage(PlayerColor.GREY);
    json = dominationBoard.serialize();
    if (!json.contains("\"redDamages\":[\"GREY\"]")){
      fail();
    }
    dominationBoard2 = DominationBoard.deserialize(json);

    if (!dominationBoard2.getBlueDamages().get(1).toString().equals(PlayerColor.GREEN.toString())){
      fail();
    }
    assertTrue(true);
  }

  @Test
  public void testCopyConstructor() {
    DominationBoard dominationBoard = new DominationBoard();
    DominationBoard dominationBoard2;

    dominationBoard.addBlueDamage(PlayerColor.PURPLE);
    dominationBoard.addBlueDamage(PlayerColor.GREEN);
    dominationBoard.addRedDamage(PlayerColor.GREY);
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 4; y++) {
        dominationBoard.setSquare(x,y,new Square(1,2,
            PlayerColor.GREEN, BorderType.WALL, BorderType.WALL, BorderType.WALL, BorderType.WALL));
      }
    }

    dominationBoard2 = new DominationBoard(dominationBoard, true);
    if (dominationBoard2 == null) {
      fail("Null pointer");
    }
    assertEquals(PlayerColor.GREY.toString(), dominationBoard2.getRedDamages().get(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    DominationBoard dominationBoard = null;
    DominationBoard dominationBoard2 = new DominationBoard(dominationBoard, true);
  }
}