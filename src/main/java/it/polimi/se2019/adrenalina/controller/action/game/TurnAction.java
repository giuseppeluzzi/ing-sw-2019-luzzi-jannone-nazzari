package it.polimi.se2019.adrenalina.controller.action.game;

/**
 * List of standard turn actions.
 */
public enum TurnAction {

  RUN("Corri", "Muoviti di 1, 2, 3 passi", false, "correre"),
  WALK_FETCH("Raccogliere", "Muoviti fino a 1 quadrato e raccogli", false, "raccogliere"),
  WALK_FETCH3("Raccogliere", "Muoviti fino a 2 quadrati e raccogli", false, Constants.WALK_AND_FETCH_DESC), // If player has at least 3 damages
  SHOOT("Spara", "Spara a un bersaglio", false, "sparare"),
  SHOOT6("Muoviti e Spara", "Muoviti fino a 1 quadrato poi spara", false, "muoversi e sparare"), // If player has at least 6 damages

  FF_RUN("Corri", "Muoviti di massimo 4 passi", true, "correre"),
  FF_RUN_FETCH("Raccogli", "Muoviti di massimo 2 passi e raccogli", true, Constants.WALK_AND_FETCH_DESC),
  FF_RUN_RELOAD_SHOOT("Cammina, ricarica e spara", "Muoviti fino a 1 quadrato, ricarica e poi spara", true, "muoversi, ricaricare e sparare"),

  FF_WALK_FETCH("Raccogli", "Muoviti di un massimo di 3 passi e raccogli", true, Constants.WALK_AND_FETCH_DESC),
  FF_WALK_RELOAD_SHOOT("Cammina, ricarica e spara", "Muoviti fino a 2 quadrati, ricarica e poi spara", true, "muoversi, ricaricare e sparare");

  private final String name;
  private final String description;
  private final boolean finalFrenzy;
  private final String message;

  TurnAction(String name, String description, boolean finalFrenzy, String message) {
    this.name = name;
    this.description = description;
    this.finalFrenzy = finalFrenzy;
    this.message = message;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getMessage() {
    return message;
  }

  public boolean isFinalFrenzy() {
    return finalFrenzy;
  }

  private static class Constants {
    public static final String WALK_AND_FETCH_DESC = "muoversi e raccogliere";
  }
}
