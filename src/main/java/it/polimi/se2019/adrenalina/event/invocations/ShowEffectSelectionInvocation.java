package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.controller.Effect;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Weapon;
import java.util.ArrayList;
import java.util.List;

public class ShowEffectSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 2421777681566272744L;
  private final Weapon weapon;
  private final List<Effect> effects;

  public ShowEffectSelectionInvocation(Weapon weapon, List<Effect> effects) {
    this.weapon = weapon;
    this.effects = new ArrayList<>(effects);
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public List<Effect> getEffects() {
    return new ArrayList<>(effects);
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_EFFECT_SELECTION_INVOCATION;
  }
}
