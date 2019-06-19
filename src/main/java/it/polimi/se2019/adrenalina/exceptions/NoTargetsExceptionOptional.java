package it.polimi.se2019.adrenalina.exceptions;

public class NoTargetsExceptionOptional extends Exception {

  private static final long serialVersionUID = 1168946040916564430L;

  public NoTargetsExceptionOptional() {

  }

  public NoTargetsExceptionOptional(String message) {
    super(message);
  }
}
