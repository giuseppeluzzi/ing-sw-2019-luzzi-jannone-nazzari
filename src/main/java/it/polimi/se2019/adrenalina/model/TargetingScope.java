package it.polimi.se2019.adrenalina.model;

public class TargetingScope extends PowerUp {
  public TargetingScope(AmmoColor color) {
    super(color);
  }

  public TargetingScope(TargetingScope targetingScope) {
    // TODO: create a copy
    super(targetingScope.getColor());
  }
  
  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }
}
