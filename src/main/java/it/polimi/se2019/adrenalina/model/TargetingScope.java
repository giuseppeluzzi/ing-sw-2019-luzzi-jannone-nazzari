package it.polimi.se2019.adrenalina.model;

public class TargetingScope extends PowerUp {
  public TargetingScope(AmmoColor color) {
    super(color);
  }

  public TargetingScope(TargetingScope powerup) {
    // TODO: copy actions
    super(powerup.getColor());
  }
  
  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }
}
