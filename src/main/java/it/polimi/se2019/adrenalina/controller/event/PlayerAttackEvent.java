package it.polimi.se2019.adrenalina.controller.event;

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

  @Override
  public String getEventName() {
    return "PlayerAttack";
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
