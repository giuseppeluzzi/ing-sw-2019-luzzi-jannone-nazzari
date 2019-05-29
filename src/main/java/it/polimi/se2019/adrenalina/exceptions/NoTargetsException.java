package it.polimi.se2019.adrenalina.exceptions;

public class NoTargetsException extends Exception {

  private static final long serialVersionUID = -7375327781566298804L;
  private final boolean rollback;

  public NoTargetsException(boolean rollback) {
    this.rollback = rollback;
  }

  public NoTargetsException(String message, boolean rollback) {
    super(message);
    this.rollback = rollback;
  }

  public boolean isRollback() {
    return rollback;
  }
}
