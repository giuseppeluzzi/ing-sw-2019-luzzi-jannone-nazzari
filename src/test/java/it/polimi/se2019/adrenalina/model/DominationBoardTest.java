package it.polimi.se2019.adrenalina.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DominationBoardTest {
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
      // TODO: testing a JSON string for a match is ugly and dangerous since JSON allows different style variations (e.g. whitespace and newlines)
      fail("Serialized JSON is not valid");
    }
    dominationBoard2 = DominationBoard.deserialize(json);

    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        PlayerColor.GREEN.toString(),
        dominationBoard2.getBlueDamages().get(1).toString()
    );
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
      fail("Board returned from copy constructor is null");
    }
    assertEquals(
        "Cloned class attributes not matching with original class attributes",
        PlayerColor.GREY.toString(),
        dominationBoard2.getRedDamages().get(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCopyConstructorException() {
    DominationBoard dominationBoard = null;
    DominationBoard dominationBoard2 = new DominationBoard(dominationBoard, true);
  }
}