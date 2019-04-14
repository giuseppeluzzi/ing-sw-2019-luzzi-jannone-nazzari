package it.polimi.se2019.adrenalina.exceptions;

public class PlayingBoardException extends Exception {
  private static final long serialVersionUID = 3861856095161132567L;

  public PlayingBoardException() {
    //
  }

  public PlayingBoardException(String message){
    super(message);
  }
}
