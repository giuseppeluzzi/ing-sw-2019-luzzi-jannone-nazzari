package it.polimi.se2019.adrenalina.model;

public class TargetingScope extends PowerUp {
  public TargetingScope(AmmoColor color) {
    super(color);
  }

  @Override
  public TargetingScope makeCopy() {
    // TODO clone object
    return null;
  }
  
  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }
}
