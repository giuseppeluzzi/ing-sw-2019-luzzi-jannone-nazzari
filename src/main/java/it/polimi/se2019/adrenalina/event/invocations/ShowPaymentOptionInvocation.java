package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.BuyableType;

public class ShowPaymentOptionInvocation implements Invocation {

  private static final long serialVersionUID = 6348887252528342959L;
  private final Buyable item;
  private final BuyableType type;

  public ShowPaymentOptionInvocation(Buyable item) {
    this.item = item;
    type = item.getBuyableType();
  }

  public BuyableType getType() {
    return type;
  }

  public Buyable getItem() {
    return item;
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_PAYMENT_OPTION_INVOCATION;
  }
}
