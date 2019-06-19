package it.polimi.se2019.adrenalina.controller.action.game;

/**
 * List of standard turn actions.
 */
public enum TurnAction {

  RUN("Corri", "Muoviti di 1, 2, 3 passi", false),
  WALK_FETCH("Raccogliere", "Muoviti fino a 1 quadrato e raccogli", false),
  WALK_FETCH3("Raccogliere", "Muoviti fino a 2 quadrati e raccogli", false), // If player has at least 3 damages
  SHOOT("Spara", "Spara a un bersaglio", false),
  SHOOT6("Muoviti e Spara", "Muoviti fino a 1 quadrato poi spara", false), // If player has at least 6 damages

  FF_RUN("Corri", "Muoviti di massimo 4 passi", true),
  FF_RUN_FETCH("Raccogli", "Muoviti di massimo 2 passi e raccogli", true),
  FF_RUN_RELOAD_SHOOT("Cammina, ricarica e spara", "Muoviti fino a 1 quadrato, ricarica e poi spara", true),

  FF_WALK_FETCH("Raccogli", "Muoviti di un massimo di 3 passi e raccogli", true),
  FF_WALK_RELOAD_SHOOT("Cammina, ricarica e spara", "Muoviti fino a 2 quadrati, ricarica e poi spara", true);

  private final String name;
  private final String description;
  private final boolean finalFrenzy;

  TurnAction(String name, String description, boolean finalFrenzy) {
    this.name = name;
    this.description = description;
    this.finalFrenzy = finalFrenzy;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isFinalFrenzy() {
    return finalFrenzy;
  }
}
