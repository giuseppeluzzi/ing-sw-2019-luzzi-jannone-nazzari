package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.BoardStatus;
import it.polimi.se2019.adrenalina.controller.BorderType;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.controller.SquareColor;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DominationBoardTest {
  private DominationBoard dominationBoard;

  @Before
  public void setDominationBoard() {
    dominationBoard = new DominationBoard();
  }

  @Test
  public void testSerialization(){
    DominationBoard dominationBoard2;
    String json;

    dominationBoard.addBlueDamage(PlayerColor.PURPLE);
    dominationBoard.addBlueDamage(PlayerColor.GREEN);
    dominationBoard.addRedDamage(PlayerColor.GREY);
    json = dominationBoard.serialize();
    if (json.isEmpty()) {
      fail("Serialized JSON is not valid");
    }
    dominationBoard2 = DominationBoard.deserialize(json);

    assertEquals(
        "Deserialized class attributes not matching with actual class attributes",
        PlayerColor.GREEN.toString(),
        dominationBoard2.getBlueDamages().get(1).toString()
    );
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSerializationException() {
    DominationBoard.deserialize(null);
  }

  @Test
  public void testAddDamages() {
    if (dominationBoard.isDominationBoard()) {
      dominationBoard.addDamage(AmmoColor.RED, PlayerColor.GREY);
      dominationBoard.addDamage(AmmoColor.BLUE, PlayerColor.GREY);
      dominationBoard.addDamage(AmmoColor.YELLOW, PlayerColor.GREY);
      dominationBoard.addDamage(AmmoColor.ANY, PlayerColor.GREY);
      assertEquals(1, dominationBoard.getRedDamages().size());
      assertEquals(1, dominationBoard.getBlueDamages().size());
      assertEquals(1, dominationBoard.getYellowDamages().size());
      List<PlayerColor> damagesUpdated = new ArrayList<>();
      damagesUpdated.add(PlayerColor.GREEN);
      damagesUpdated.add(PlayerColor.GREY);
      dominationBoard.updateDamages(AmmoColor.RED, damagesUpdated);
      dominationBoard.updateDamages(AmmoColor.BLUE, damagesUpdated);
      dominationBoard.updateDamages(AmmoColor.YELLOW, damagesUpdated);
      assertEquals(2, dominationBoard.getRedDamages().size());
      assertEquals(2, dominationBoard.getBlueDamages().size());
      assertEquals(2, dominationBoard.getYellowDamages().size());
      assertEquals(new Integer(2), dominationBoard.getSpawnPointDamages().get(AmmoColor.RED));
    }
  }

  @Test
  public void testCheckEnableFrenzy() {
    List<PlayerColor> damages = new ArrayList<>();

    for (int i = 0; i < 8; i++) {
      damages.add(PlayerColor.GREEN);
    }
    dominationBoard.setFinalFrenzySelected(true);
    dominationBoard.setStatus(BoardStatus.MATCH);
    dominationBoard.updateDamages(AmmoColor.RED, damages);
    damages.remove(0);
    dominationBoard.updateDamages(AmmoColor.BLUE, damages);
    dominationBoard.addBlueDamage(PlayerColor.GREEN);
    assertEquals(BoardStatus.FINAL_FRENZY_ENABLED, dominationBoard.getStatus());
  }
}