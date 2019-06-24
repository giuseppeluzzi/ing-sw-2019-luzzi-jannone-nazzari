package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.PowerUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Invocation to print the player's payment options on the client.
 */
public class ShowPaymentOptionInvocation implements Invocation {

  private static final long serialVersionUID = 6348887252528342959L;
  private final BuyableType buyableType;
  private final HashMap<AmmoColor, Integer> buyableCost;
  private final List<PowerUp> budgetPowerUps;
  private final HashMap<AmmoColor, Integer> budgetAmmos;
  private final String prompt;

  public ShowPaymentOptionInvocation(BuyableType buyableType, String prompt, Map<AmmoColor, Integer> buyableCost,
      List<PowerUp> budgetPowerUps, Map<AmmoColor, Integer> budgetAmmos) {
    this.buyableType = buyableType;
    this.buyableCost = new HashMap<>(buyableCost);
    this.budgetPowerUps = new ArrayList<>(budgetPowerUps);
    this.budgetAmmos = new HashMap<>(budgetAmmos);
    this.prompt = prompt;
  }

  public BuyableType getBuyableType() {
    return buyableType;
  }

  public Map<AmmoColor, Integer> getBuyableCost() {
    return new HashMap<>(buyableCost);
  }

  public List<PowerUp> getBudgetPowerUps() {
    return new ArrayList<>(budgetPowerUps);
  }

  public Map<AmmoColor, Integer> getBudgetAmmos() {
    return new HashMap<>(budgetAmmos);
  }

  public String getPrompt() {
    return prompt;
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_PAYMENT_OPTION_INVOCATION;
  }
}
