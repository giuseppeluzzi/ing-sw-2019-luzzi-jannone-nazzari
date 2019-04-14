package it.polimi.se2019.adrenalina.view;

import it.polimi.se2019.adrenalina.controller.event.Event;
import it.polimi.se2019.adrenalina.controller.event.PlayerDeathEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerMoveEvent;
import it.polimi.se2019.adrenalina.controller.event.PlayerSpawnEvent;
import it.polimi.se2019.adrenalina.model.Player;
import java.util.List;

public interface CharactersViewInterface {

  public List<Player> getPlayers();

  public void addPlayer(Player player);

  public void setSelected(Player player);

  public void removeSelected();

  public void update(PlayerDeathEvent event);

  public void update(PlayerSpawnEvent event);

  public void update(PlayerMoveEvent event);

  public void update(Event event);
}
