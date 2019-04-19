package it.polimi.se2019.adrenalina.exceptions;

public class WrongConfigurationException extends Exception {
  private static final long serialVersionUID = 2073062723404462196L;

  public WrongConfigurationException() {
    //
  }

  public WrongConfigurationException(String message){
    super(message);
  }
}
