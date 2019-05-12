package it.polimi.se2019.adrenalina.controller.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.model.AmmoCard;
import it.polimi.se2019.adrenalina.model.Square;
import java.io.Serializable;

public class AmmoCardUpdateEvent implements Event, Serializable {
  private final Square square;
  private final AmmoCard ammoCard;

  public AmmoCardUpdateEvent(Square square, AmmoCard ammoCard) {
    this.square = square;
    this.ammoCard = ammoCard;
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.AMMO_CARD_UPDATE_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public static AmmoCardUpdateEvent deserialize(String json) {
    if (json == null) {
      throw new IllegalArgumentException("Argument json can't be null");
    }
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
