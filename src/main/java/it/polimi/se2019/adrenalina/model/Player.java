package it.polimi.se2019.adrenalina.model;
import java.util.Collection;
import java.util.Observable;

public class Player extends Observable implements Target {

  private Collection<Player> damages;
  private Collection<Player> tags;
  private AmmoColor[] ammo;
  private PlayerColor color;
  private int points;
  private int deaths;
  private Square square;
  private String name;
  private Collection<PowerUp> powerUps;
  private Collection<Weapon> weapons;
  private PlayerStatus status;
  private boolean frenzy;

  public Player(PlayerColor color, String name) {
    this.color = color;
    this.name = name;

    // TODO: instanciate other attributes

  }

  // TODO: getters and setters

  @Override
  public boolean isPlayer() {
    return true;
  }

  // TODO: observer methods

}
