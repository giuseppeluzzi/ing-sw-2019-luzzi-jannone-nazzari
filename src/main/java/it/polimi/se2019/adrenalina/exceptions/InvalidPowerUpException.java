package it.polimi.se2019.adrenalina.exceptions;

public class InvalidPowerUpException extends Exception {
  private static final long serialVersionUID = 6874957593635251253L;

  public InvalidPowerUpException() {
    //
  }

  public InvalidPowerUpException(String message) {
    super(message);
  }
}
