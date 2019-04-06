package it.polimi.se2019.adrenalina.model;

public class Newton extends PowerUp {
  public Newton(AmmoColor color) {
    super(color);
  }

  public Newton(Newton newton) {
    // TODO: create a copy
    super(newton.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }
}




