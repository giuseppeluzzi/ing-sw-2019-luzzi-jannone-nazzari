package it.polimi.se2019.adrenalina.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.controller.MoveAction;
import it.polimi.se2019.adrenalina.controller.OptionalMoveAction;
import it.polimi.se2019.adrenalina.controller.SelectAction;
import it.polimi.se2019.adrenalina.controller.SelectDirectionAction;
import it.polimi.se2019.adrenalina.controller.ShootAction;
import it.polimi.se2019.adrenalina.controller.ActionType;
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
        jsonObject.get("costYellow").getAsInt());

    for (JsonElement action : jsonObject.get("actions").getAsJsonArray()) {
      JsonObject actionObj = action.getAsJsonObject();
      switch (ActionType.valueOf(actionObj.get("type").getAsString())) {
        case SELECT:
          effect.addAction(new SelectAction(
              actionObj.get("from").getAsInt(),
              actionObj.get("target").getAsInt(),
              actionObj.get("minDistance").getAsInt(),
              actionObj.get("maxDistance").getAsInt(),
              actionObj.get("differentFrom").getAsInt(),
              actionObj.get("visible").getAsBoolean()));
          break;
        case SELECT_DIRECTION:
          effect.addAction(new SelectDirectionAction());
          break;
        case SHOOT:
          effect.addAction(new ShootAction(
              actionObj.get("target").getAsInt(),
              actionObj.get("damages").getAsInt(),
              actionObj.get("tag").getAsInt()
          ));
          break;
        case MOVE:
          effect.addAction(new MoveAction(
              actionObj.get("target").getAsInt(),
              actionObj.get("destination").getAsInt()
          ));
          break;
        case OPTIONAL_MOVE:
          effect.addAction(new OptionalMoveAction(
              actionObj.get("target").getAsInt(),
              actionObj.get("destination").getAsInt()
          ));
          break;
        default:
          Log.severe("Unsupported type of effect action");
      }
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
