package it.polimi.se2019.adrenalina.model;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuyableTest {
  private Buyable buyable;

  @Before
  public void setBuyable() {
    buyable = new Weapon(1,2,1,AmmoColor.YELLOW,"test","w");
  }

  @Test
  public void testGetCost() {
    buyable.getBuyableType();
    buyable.getCost();
    buyable.promptMessage();
    assertEquals(2, buyable.getCost(AmmoColor.BLUE));
  }

}