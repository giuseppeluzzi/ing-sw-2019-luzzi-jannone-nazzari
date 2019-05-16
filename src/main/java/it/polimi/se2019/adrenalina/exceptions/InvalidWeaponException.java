package it.polimi.se2019.adrenalina.exceptions;

public class InvalidWeaponException extends Exception {
  private static final long serialVersionUID = -3767720639935729878L;

  public InvalidWeaponException() {
    //
  }

  public InvalidWeaponException(String message) {
    super(message);
  }
}
