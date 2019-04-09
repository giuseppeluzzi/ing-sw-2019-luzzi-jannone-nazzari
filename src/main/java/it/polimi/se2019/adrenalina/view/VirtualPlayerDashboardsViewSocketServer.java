package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerUpdateEvent;
import it.polimi.se2019.adrenalina.model.Player;
import java.lang.invoke.WrongMethodTypeException;

public class VirtualPlayerDashboardsViewSocketServer extends PlayerDashboardsView {

  @Override
  public void reset(Player player) {
    // TODO: reset the player dashboard
  }

  @Override
  public void switchToFinalFrenzy(Player player) {
    // TODO: change dashboard to final frenzy mode
  }

  @Override
  public void update(PlayerUpdateEvent event) {
    // TODO: update the status of the player dashboard
  }

  @Override
  public void update(Event event) {
    throw new WrongMethodTypeException();
  }

}
