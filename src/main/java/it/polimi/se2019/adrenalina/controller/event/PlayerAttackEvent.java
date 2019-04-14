package it.polimi.se2019.adrenalina.controller.event;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.model.Player;

public class PlayerAttackEvent implements Event {
  private final Player player;
  private final Player target;
  private final Effect effect;

  public PlayerAttackEvent(Player player, Player target,
      Effect effect) {
    this.player = player;
    this.target = target;
    this.effect = effect;
  }

  public static PlayerAttackEvent deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, PlayerAttackEvent.class);
  }

  @Override
  public String serialize() {
    Gson gson = new Gson();
    JsonElement jsonElement = gson.toJsonTree(this);
    jsonElement.getAsJsonObject().addProperty("eventType", EventType.PLAYER_ATTACK_EVENT.toString());
    return gson.toJson(jsonElement);
  }

  public Player getPlayer() {
    return player;
  }

  public Player getTarget() {
    return target;
  }

  public Effect getEffect() {
    return effect;
  }
}
