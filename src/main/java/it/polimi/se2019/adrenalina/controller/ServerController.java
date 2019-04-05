package it.polimi.se2019.adrenalina.controller;

import java.util.ArrayList;
import java.util.List;

public class ServerController {
  private List<BoardController> games;

  public ServerController() {
    games = new ArrayList<>();

  }

  public void createGame(BoardController controller) {
    //TODO create thread and append to ArrayList
  }

  public BoardController getPendingGame(boolean domination) {
    //TODO: find pending game with same mode with at least one free spot
    return null;
  }


}
