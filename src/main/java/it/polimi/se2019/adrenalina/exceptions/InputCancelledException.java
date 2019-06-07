package it.polimi.se2019.adrenalina.exceptions;

public class InputCancelledException extends Exception {

  private static final long serialVersionUID = -6059964253283841608L;

  public InputCancelledException() {
    //
  }

  public InputCancelledException(String message) {
    super(message);
  }

}
