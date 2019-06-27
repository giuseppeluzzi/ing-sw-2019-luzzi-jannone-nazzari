package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import org.junit.Test;

public class PaymentTest {

  @Test
  public void testFree() {
    Player player = new Player("test", PlayerColor.GREEN, null);
    Payment payment = new Payment(null, player, new Newton(AmmoColor.RED));
    assertTrue(payment.isFree());
  }
}