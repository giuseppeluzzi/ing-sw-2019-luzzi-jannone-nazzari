package it.polimi.se2019.adrenalina.model;

public class Newton extends PowerUp {
  public Newton(AmmoColor color) {
    super(color);
  }

  @Override
  public Newton makeCopy() {
    // TODO: copy object
    return null;
  }

  @Override
  public boolean canUse(){
    //TODO: implement function
    return true;
  }
}




