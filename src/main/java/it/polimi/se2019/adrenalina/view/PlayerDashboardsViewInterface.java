package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerUpdateEvent;
import it.polimi.se2019.adrenalina.model.Player;
import java.util.List;

public interface PlayerDashboardsViewInterface {

  void addPlayer(Player player);

  List<Player> getPlayers();

  void reset(Player player);

  void switchToFinalFrenzy(Player player);

  void update(PlayerUpdateEvent event);

  void update(Event event);
}
