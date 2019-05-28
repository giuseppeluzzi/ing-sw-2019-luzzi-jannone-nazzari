package it.polimi.se2019.adrenalina.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.event.invocations.ShowPaymentOptionInvocation;
import it.polimi.se2019.adrenalina.model.Buyable;
import it.polimi.se2019.adrenalina.model.BuyableType;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import it.polimi.se2019.adrenalina.model.Teleporter;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.lang.reflect.Type;

public class JsonShowPaymentOptionInvocationDeserializer implements
    JsonDeserializer<ShowPaymentOptionInvocation> {

  @Override
  public ShowPaymentOptionInvocation deserialize(JsonElement jsonElement, Type type,
      JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    Buyable buyableItem = null;

    Log.debug(jsonObject.toString());
    switch (BuyableType.valueOf(jsonObject.get("type").getAsString())) {
      case WEAPON:
        buyableItem = jsonDeserializationContext.deserialize(jsonObject.get("item"), Weapon.class);
        break;
      case POWERUP:
        PowerUp powerUp = jsonDeserializationContext
            .deserialize(jsonObject.get("item"), PowerUp.class);

        switch (powerUp.getType()) {
          case NEWTON:
            buyableItem = new Newton(powerUp.getColor());
            break;
          case TELEPORTER:
            buyableItem = new Teleporter(powerUp.getColor());
            break;
          case TAGBACK_GRANADE:
            buyableItem = new TagbackGrenade(powerUp.getColor());
            break;
          case TARGETING_SCOPE:
            buyableItem = new TargetingScope(powerUp.getColor());
            break;
        }
        break;
      case EFFECT:
        buyableItem = jsonDeserializationContext.deserialize(jsonObject.get("item"), Effect.class);
        break;
    }

    return new ShowPaymentOptionInvocation(buyableItem);
  }
}
