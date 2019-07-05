package it.polimi.se2019.adrenalina.controller;

/**
 * Application configuration singleton read from external file.
 */
public interface Configuration {

  Integer getTurnTimeout();

  Integer getRmiPort();

  Integer getSocketPort() ;

  Integer getMinNumPlayers();

}
