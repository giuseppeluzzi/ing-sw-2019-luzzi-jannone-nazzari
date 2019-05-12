package it.polimi.se2019.adrenalina.exceptions;

public class InvalidSquareException extends Exception {
  private static final long serialVersionUID = 8377053571757675547L;

  public InvalidSquareException() {
    //
  }

  public InvalidSquareException(String message) {
    super(message);
  }
}
