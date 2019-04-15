package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.model.Player;
import java.util.List;

public interface CharactersViewInterface {

  List<Player> getPlayers();

  void addPlayer(Player player);

  void setSelected(Player player);

  void removeSelected();

  void update(PlayerDeathEvent event);

  void update(PlayerSpawnEvent event);

  void update(PlayerMoveEvent event);

  void update(Event event);
}
