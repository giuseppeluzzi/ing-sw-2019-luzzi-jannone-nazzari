package it.polimi.se2019.adrenalina.event.viewcontroller;

import it.polimi.se2019.adrenalina.controller.PlayerColor;
import it.polimi.se2019.adrenalina.event.Event;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.PowerUp;

import java.util.ArrayList;
import java.util.List;

/**
 * Event fired when a player completes a payment.
 */
public class PlayerPaymentEvent implements Event {

  private static final long serialVersionUID = -188103349321829299L;
  private final PlayerColor playerColor;
  private final int red;
  private final int blue;
  private final int yellow;
  private final List<PowerUp> powerUps;

  public PlayerPaymentEvent(PlayerColor playerColor, int red, int blue, int yellow,
      List<PowerUp> powerUps) {
    this.playerColor = playerColor;
    this.red = red;
    this.blue = blue;
    this.yellow = yellow;
    this.powerUps = new ArrayList<>(powerUps);
  }

  public PlayerColor getPlayerColor() {
    return playerColor;
  }

  public int getRed() {
    return red;
  }

  public int getBlue() {
    return blue;
  }

  public int getYellow() {
    return yellow;
  }

  public List<PowerUp> getPowerUps() {
    return new ArrayList<>(powerUps);
  }

  @Override
  public EventType getEventType() {
    return EventType.PLAYER_PAYMENT_EVENT;
  }
}
