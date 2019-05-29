package it.polimi.se2019.adrenalina.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.PowerUpType;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import it.polimi.se2019.adrenalina.model.Teleporter;
import java.lang.reflect.Type;

public class JsonPowerUpDeserializer implements JsonDeserializer<PowerUp> {

  @Override
  public PowerUp deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    PowerUpType powerUpType = PowerUpType.valueOf(jsonObject.get("type").getAsString());
    AmmoColor powerUpColor = AmmoColor.valueOf(jsonObject.get("color").getAsString());

    switch (powerUpType) {
      case NEWTON:
        return new Newton(powerUpColor);
      case TELEPORTER:
        return new Teleporter(powerUpColor);
      case TAGBACK_GRANADE:
        return new TagbackGrenade(powerUpColor);
      case TARGETING_SCOPE:
        return new TargetingScope(powerUpColor);
    }
    return null;
  }
}
