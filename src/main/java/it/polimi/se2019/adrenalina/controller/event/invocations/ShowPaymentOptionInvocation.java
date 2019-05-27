package it.polimi.se2019.adrenalina.controller.event.invocations;

import it.polimi.se2019.adrenalina.controller.event.EventType;
import it.polimi.se2019.adrenalina.model.Buyable;

public class ShowPaymentOptionInvocation implements Invocation {

  private static final long serialVersionUID = 6348887252528342959L;
  private final Buyable item;

  public ShowPaymentOptionInvocation(Buyable item) {
    this.item = item;
  }

  public Buyable getItem() {
    return item;
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_PAYMENT_OPTION_INVOCATION;
  }
}
