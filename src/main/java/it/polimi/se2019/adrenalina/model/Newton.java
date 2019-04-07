package it.polimi.se2019.adrenalina.model;

public class Newton extends PowerUp {
  public Newton(AmmoColor color) {
    super(color);
  }

  public Newton(Newton powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }
}




