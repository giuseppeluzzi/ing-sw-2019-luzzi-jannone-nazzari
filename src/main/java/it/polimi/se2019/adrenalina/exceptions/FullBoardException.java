package it.polimi.se2019.adrenalina.exceptions;

public class FullBoardException extends Exception {
  private static final long serialVersionUID = 8377053571757675547L;

  public FullBoardException() {
    //
  }

  public FullBoardException(String message) {
    super(message);
  }
}
