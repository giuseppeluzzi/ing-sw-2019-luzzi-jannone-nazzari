package it.polimi.se2019.adrenalina.model;

import java.util.ArrayList;
import java.util.List;

public class Square extends Target {

  private final int posX;
  private final int posY;
  private final PlayerColor color;

  private boolean spawnPoint;
  private AmmoCard ammoCard;

  private final Material[] edges;
  private final List<Weapon> weapons;

  public Square(int posX, int posY, PlayerColor color,
      Material edgeUp, Material edgeRight,
      Material edgeDown, Material edgeLeft) {

    this.posX = posX;
    this.posY = posY;
    this.color = color;

    spawnPoint = false;
    ammoCard = null;

    edges = new Material[4];
    edges[Direction.UP.ordinal()] = edgeUp;
    edges[Direction.RIGHT.ordinal()] = edgeRight;
    edges[Direction.DOWN.ordinal()] = edgeDown;
    edges[Direction.LEFT.ordinal()] = edgeLeft;

    weapons = new ArrayList<>();
  }

  @Override
  public boolean isPlayer() {
    return false;
  }

  public int getPosX() {
    return posX;
  }

  public int getPosY() {
    return posY;
  }

  public PlayerColor getColor() {
    return color;
  }

  public boolean isSpawnPoint() {
    return spawnPoint;
  }

  public void setSpawnPoint(boolean spawnPoint) {
    this.spawnPoint = spawnPoint;
  }

  public boolean hasAmmoCard() {
    return ammoCard != null;
  }

  public AmmoCard getAmmoCard() {
    return ammoCard;
  }

  public void setAmmoCard(AmmoCard ammoCard) {
    this.ammoCard = ammoCard;
  }

  public Material getEdge(Direction direction) {
    return edges[direction.ordinal()];
  }

  // TODO: implement getWeapons, weapons is mutable

  public void addWeapon(Weapon weapon) {
    // TODO: exception if this square is a spawnpoint
    weapons.add(weapon);
  }


}
