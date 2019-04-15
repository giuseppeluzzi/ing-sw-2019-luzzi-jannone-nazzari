package it.polimi.se2019.adrenalina.exceptions;

public class EndedGameException extends Exception {
  private static final long serialVersionUID = 4153795626157986011L;

  public EndedGameException() {
    //
  }

  public EndedGameException(String message) {
    super(message);
  }
}
