package it.polimi.se2019.adrenalina.model;

public class TagbackGrenade extends PowerUp {
  public TagbackGrenade(AmmoColor color) {
    super(color);
  }

  @Override
  public TagbackGrenade makeCopy() {
    // TODO copy object
    return null;
  }

  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }
}