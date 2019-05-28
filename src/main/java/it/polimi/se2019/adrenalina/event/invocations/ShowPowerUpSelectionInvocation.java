package it.polimi.se2019.adrenalina.event.invocations;

import it.polimi.se2019.adrenalina.controller.AmmoColor;
import it.polimi.se2019.adrenalina.event.EventType;
import it.polimi.se2019.adrenalina.model.Newton;
import it.polimi.se2019.adrenalina.model.PowerUp;
import it.polimi.se2019.adrenalina.model.TagbackGrenade;
import it.polimi.se2019.adrenalina.model.TargetingScope;
import it.polimi.se2019.adrenalina.model.Teleporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowPowerUpSelectionInvocation implements Invocation {

  private static final long serialVersionUID = 9171282818942368354L;
  private final Map<String, List<AmmoColor>> powerUps;

  public ShowPowerUpSelectionInvocation(Map<String, List<AmmoColor>> powerUps) {
    this.powerUps = new HashMap<>(powerUps);
  }

  public List<PowerUp> getPowerUps() {
    List<PowerUp> returnList = new ArrayList<>();
    for (Map.Entry<String, List<AmmoColor>> entry : powerUps.entrySet()) {
      String key = entry.getKey();
      List<AmmoColor> ammoColors = entry.getValue();
      addPowerUp(returnList, key, ammoColors);
    }
    return returnList;
  }

  private void addPowerUp(List<PowerUp> returnList, String key, List<AmmoColor> ammoColors) {
    if ("Granata a frammentazione".equals(key)) {
      for (AmmoColor color : ammoColors) {
        returnList.add(new TagbackGrenade(color));
      }
    } else if ("Mirino".equals(key)) {
      for (AmmoColor color : ammoColors) {
        returnList.add(new TargetingScope(color));
      }
    } else if ("Teletrasporto".equals(key)) {
      for (AmmoColor color : ammoColors) {
        returnList.add(new Teleporter(color));
      }
    } else if ("Raggio traente".equals(key)) {
      for (AmmoColor color : ammoColors) {
        returnList.add(new Newton(color));
      }
    }
  }

  @Override
  public EventType getEventType() {
    return EventType.SHOW_POWER_UP_SELECTION_INVOCATION;
  }
}
