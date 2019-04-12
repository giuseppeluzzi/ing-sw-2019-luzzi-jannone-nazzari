package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Square;

public class AmmoCardUpdateEvent implements Event {
  private final Square square;
  private final AmmoCard ammoCard;
  private final EventType eventType;

  public AmmoCardUpdateEvent(Square square, AmmoCard ammoCard) {
    this.square = square;
    this.ammoCard = ammoCard;
    eventType = EventType.AMMO_CARD_UPDATE_EVENT;
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.AMMO_CARD_UPDATE_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  @Override
  public AmmoCardUpdateEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, AmmoCardUpdateEvent.class);
  }

  public Square getSquare() {
    return square;
  }

  public AmmoCard getAmmoCard() {
    return ammoCard;
  }
}
