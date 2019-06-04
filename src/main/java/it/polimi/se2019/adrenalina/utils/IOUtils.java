package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.controller.Configuration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtils {

  public static String readFile(String path) throws IOException {
    StringBuilder text = new StringBuilder(1);
    BufferedReader stream = new BufferedReader(new InputStreamReader(
        Configuration.class.getClassLoader().getResourceAsStream(path)));
    String line = "";
    while ((line = stream.readLine()) != null) {
      text.append(line);
    }
    return text.toString();
  }

}
