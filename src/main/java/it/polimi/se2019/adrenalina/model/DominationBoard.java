package it.polimi.se2019.adrenalina.model;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class DominationBoard extends Board {
  private final List<Kill> blue;
  private final List<Kill> red;
  private final List<Kill> yellow;

  public DominationBoard() {
    blue = new ArrayList<>();
    red = new ArrayList<>();
    yellow = new ArrayList<>();
  }

  public DominationBoard(DominationBoard dominationBoard, boolean publicCopy) {
    super(dominationBoard, publicCopy);
    blue = dominationBoard.getBlue();
    red = dominationBoard.getRed();
    yellow = dominationBoard.getYellow();
  }

  public void addBlueKill(Kill kill) {
    blue.add(kill);
  }

  public List<Kill> getBlue() {
    List<Kill> output = new ArrayList<>();
    for (Kill kill : blue) {
      output.add(new Kill(kill));
    }
    return output;
  }

  public void addRedKill(Kill kill) {
    red.add(kill);
  }

  public List<Kill> getRed() {
    List<Kill> output = new ArrayList<>();
    for (Kill kill : red) {
      output.add(new Kill(kill));
    }
    return output;
  }

  public void addYellowKill(Kill kill) {
    yellow.add(kill);
  }

  public List<Kill> getYellow() {
    List<Kill> output = new ArrayList<>();
    for (Kill kill : yellow) {
      output.add(new Kill(kill));
    }
    return output;
  }

  @Override
  public boolean isDominationBoard() {
    return true;
  }

  public String serialize() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public Player deserialize(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Player.class);
  }
}
