package it.polimi.se2019.adrenalina.model;

public class TagbackGrenade extends PowerUp {
  public TagbackGrenade(AmmoColor color) {
    super(color);
  }

  public TagbackGrenade(TagbackGrenade tagbackGrenade) {
    // TODO: create a copy
    super(tagbackGrenade.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }
}