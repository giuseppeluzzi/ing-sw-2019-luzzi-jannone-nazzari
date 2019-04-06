package it.polimi.se2019.adrenalina.model;

public class Teleporter extends PowerUp {
  public Teleporter(AmmoColor color) {
    super(color);
  }

  public Teleporter(Teleporter teleporter) {
    // TODO: create a copy
    super(teleporter.getColor());
  }

  @Override
  public boolean canUse(){
    //TODO implement function
    return true;
  }
}