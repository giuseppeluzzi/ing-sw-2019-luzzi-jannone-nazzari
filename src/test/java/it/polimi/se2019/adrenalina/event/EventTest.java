package it.polimi.se2019.adrenalina.event;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventTest {

  private PlayerConnectEvent connectEvent;

  @Before
  public void setUp() {
    connectEvent = new PlayerConnectEvent("TestPlayer", true);
  }

  @Test
  public void serialize() {
    Gson gson = new Gson();
    String serializedEvent = connectEvent.serialize();
    JsonObject jsonEvent = gson.fromJson(serializedEvent, JsonObject.class);
    PlayerConnectEvent deserializedEvent = gson.fromJson(serializedEvent, PlayerConnectEvent.class);

    assertEquals(EventType.PLAYER_CONNECT_EVENT.toString(),
        jsonEvent.get("eventType").getAsString());
    assertEquals("TestPlayer", deserializedEvent.getPlayerName());
    assertTrue(deserializedEvent.isDomination());
  }
}