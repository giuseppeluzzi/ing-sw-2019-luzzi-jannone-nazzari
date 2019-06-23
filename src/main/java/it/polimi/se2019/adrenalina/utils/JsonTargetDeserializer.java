package it.polimi.se2019.adrenalina.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.model.Player;
import it.polimi.se2019.adrenalina.model.Square;
import it.polimi.se2019.adrenalina.model.Target;

import java.lang.reflect.Type;

/**
 * Target deserialization from a JSON string.
 */
public class JsonTargetDeserializer implements JsonDeserializer<Target> {

  @Override
  public Target deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    Target target;
    if (jsonObject.has("spawnPoint")) {
      target = jsonDeserializationContext.deserialize(jsonElement, Square.class);
    } else {
      target = jsonDeserializationContext.deserialize(jsonElement, Player.class);
    }
    return target;
  }
}
