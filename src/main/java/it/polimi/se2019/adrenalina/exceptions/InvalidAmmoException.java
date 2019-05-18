package it.polimi.se2019.adrenalina.exceptions;

public class InvalidAmmoException extends Exception {
  private static final long serialVersionUID = 157688142785639246L;

  public InvalidAmmoException() {
    //
  }

  public InvalidAmmoException(String message) {
    super(message);
  }
}
