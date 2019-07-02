package it.polimi.se2019.adrenalina.utils;

import it.polimi.se2019.adrenalina.controller.ServerConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Input/output file utilities.
 */
public class IOUtils {

  private IOUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static String readResourceFile(String path) throws IOException {
    StringBuilder text = new StringBuilder(1);
    BufferedReader stream = new BufferedReader(new InputStreamReader(
        ServerConfig.class.getClassLoader().getResourceAsStream(path)));
    String line = "";
    while ((line = stream.readLine()) != null) {
      text.append(line);
    }
    return text.toString();
  }

  public static void loadClientConfiguration() throws IOException {
    File extConfig = new File(Constants.CLIENT_CONFIG_FILE);
    if (! extConfig.isFile()) {
      byte[] intConfig = readResourceFile(Constants.CLIENT_CONFIG_FILE).getBytes(StandardCharsets.UTF_8);
      Path extFile = Paths.get(Constants.CLIENT_CONFIG_FILE);
      Files.write(extFile, intConfig);
    }
  }
}
