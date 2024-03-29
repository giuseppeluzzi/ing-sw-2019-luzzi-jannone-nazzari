package it.polimi.se2019.adrenalina.utils;

import com.google.gson.*;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.action.weapon.WeaponActionType;

import java.lang.reflect.Type;

/**
 * Effect deserialization from a JSON string.
 */
public class JsonEffectDeserializer implements JsonDeserializer<Effect> {
  @Override
  public Effect deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext context) {
    String anyTime = "anyTime";
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    if (! jsonObject.has(anyTime)) {
      jsonObject.addProperty(anyTime, false);
    }

    Effect effect = new Effect(
        jsonObject.get("name").getAsString(),
        null,
        jsonObject.get("costRed").getAsInt(),
        jsonObject.get("costBlue").getAsInt(),
        jsonObject.get("costYellow").getAsInt(),
        jsonObject.get(anyTime).getAsBoolean());

    for (JsonElement action : jsonObject.get("actions").getAsJsonArray()) {
      JsonObject actionObj = action.getAsJsonObject();
      effect.addAction(
          context.deserialize(actionObj,
              WeaponActionType.valueOf(actionObj.get("type").getAsString()).getActionClass()));
    }

    if (jsonObject.has("subEffects")) {
      GsonBuilder builder = new GsonBuilder();
      JsonDeserializer<Effect> effectJsonDeserializer = new JsonEffectDeserializer();
      builder.registerTypeAdapter(Effect.class, effectJsonDeserializer);
      Gson gsonEffect = builder.create();

      for (JsonElement subEffect: jsonObject.get("subEffects").getAsJsonArray()) {
        effect.addSubEffect(gsonEffect.fromJson(subEffect, Effect.class));
      }
    }

    return effect;
  }
}
