package it.polimi.se2019.adrenalina.model;

public class Teleporter extends PowerUp {
  public Teleporter(AmmoColor color) {
    super(color);
  }

  @Override
  public Teleporter makeCopy() {
    // TODO clone object
    return null;
  }

  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }
}