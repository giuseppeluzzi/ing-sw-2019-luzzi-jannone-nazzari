package it.polimi.se2019.adrenalina.model;

public class TagbackGrenade extends PowerUp {
  public TagbackGrenade(AmmoColor color) {
    super(color);
  }

  public TagbackGrenade(TagbackGrenade powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }
}