package it.polimi.se2019.adrenalina.exceptions;

public class InvalidPlayerException extends Exception {
  private static final long serialVersionUID = 8377053571757675547L;

  public InvalidPlayerException() {
    //
  }

  public InvalidPlayerException(String message) {
    super(message);
  }
}
