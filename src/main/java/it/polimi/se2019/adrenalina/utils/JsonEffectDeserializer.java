package it.polimi.se2019.adrenalina.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.action.MoveAction;
import it.polimi.se2019.adrenalina.controller.action.OptionalMoveAction;
import it.polimi.se2019.adrenalina.controller.action.SelectAction;
import it.polimi.se2019.adrenalina.controller.action.SelectDirectionAction;
import it.polimi.se2019.adrenalina.controller.action.ShootAction;
import it.polimi.se2019.adrenalina.controller.action.ActionType;
import it.polimi.se2019.adrenalina.controller.action.ShootRoomAction;
import it.polimi.se2019.adrenalina.controller.action.ShootSquareAction;
import java.lang.reflect.Type;

public class JsonEffectDeserializer implements JsonDeserializer<Effect> {
  @Override
  public Effect deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext context) {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    Effect effect = new Effect(
        jsonObject.get("name").getAsString(),
        null,
        jsonObject.get("costRed").getAsInt(),
        jsonObject.get("costBlue").getAsInt(),
        jsonObject.get("costYellow").getAsInt(),
        jsonObject.get("anyTime").getAsBoolean());

    for (JsonElement action : jsonObject.get("actions").getAsJsonArray()) {
      JsonObject actionObj = action.getAsJsonObject();
      effect.addAction(
          context.deserialize(actionObj,
              ActionType.valueOf(actionObj.get("type").getAsString()).getActionClass()));
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
