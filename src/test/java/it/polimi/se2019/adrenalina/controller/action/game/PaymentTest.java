package it.polimi.se2019.adrenalina.controller.action.game;

import static org.junit.Assert.*;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.Spendable;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class PaymentTest {

  @Test
  public void testFree() {
    Player player = new Player("test", PlayerColor.GREEN, null);
    Payment payment = new Payment(null, player, new Newton(AmmoColor.RED));
    assertTrue(payment.isFree());
  }

  @Test
  public void testVerifyPaymentAnswers() {
    Map<AmmoColor, Integer> budgetAmmo = new EnumMap<>(AmmoColor.class);
    Map<AmmoColor, Integer> buyableCost = new EnumMap<>(AmmoColor.class);
    List<PowerUp> powerUps = new ArrayList<>();
    powerUps.add(new Newton(AmmoColor.BLUE));
    powerUps.add(new TagbackGrenade(AmmoColor.YELLOW));
    budgetAmmo.put(AmmoColor.RED, 2);
    budgetAmmo.put(AmmoColor.BLUE, 0);
    budgetAmmo.put(AmmoColor.YELLOW, 1);
    buyableCost.put(AmmoColor.BLUE, 0);
    buyableCost.put(AmmoColor.RED, 1);
    buyableCost.put(AmmoColor.YELLOW, 1);
    buyableCost.put(AmmoColor.ANY, 1);
    List<Spendable> spendables = Player.setSpendable(powerUps, budgetAmmo);
    assertEquals(java.util.Optional.of(-1),
        java.util.Optional.of(Payment.verifyPaymentAnswers(
            new HashSet<>(), spendables)));
    Set<String> answers = new HashSet<>();
    answers.add("0");
    assertEquals(java.util.Optional.of(0),
        java.util.Optional.of(Payment.verifyPaymentAnswers(
            answers, spendables)));
    answers.add("11");
    assertEquals(java.util.Optional.of(-1),
        java.util.Optional.of(Payment.verifyPaymentAnswers(
            answers, spendables)));
  }

  @Test
  public void testVerifyPaymentFullfilled() {
    Map<AmmoColor, Integer> budgetAmmo = new EnumMap<>(AmmoColor.class);
    Map<AmmoColor, Integer> buyableCost = new EnumMap<>(AmmoColor.class);
    List<PowerUp> powerUps = new ArrayList<>();
    powerUps.add(new Newton(AmmoColor.BLUE));
    powerUps.add(new TagbackGrenade(AmmoColor.YELLOW));
    budgetAmmo.put(AmmoColor.RED, 2);
    budgetAmmo.put(AmmoColor.BLUE, 0);
    budgetAmmo.put(AmmoColor.YELLOW, 1);
    buyableCost.put(AmmoColor.BLUE, 0);
    buyableCost.put(AmmoColor.RED, 1);
    buyableCost.put(AmmoColor.YELLOW, 1);
    buyableCost.put(AmmoColor.ANY, 1);
    List<Spendable> spendables = Player.setSpendable(powerUps, budgetAmmo);
    Set<String> answers = new HashSet<>();
    answers.add("0");
    answers.add("1");
    answers.add("2");
    assertEquals(
        java.util.Optional.of(0),
        java.util.Optional.of(Payment.verifyPaymentFullfilled(
            answers, spendables, buyableCost)));
    assertEquals(
        java.util.Optional.of(-1),
        java.util.Optional.of(Payment.verifyPaymentFullfilled(
            new HashSet<>(), spendables, buyableCost)));
  }
}