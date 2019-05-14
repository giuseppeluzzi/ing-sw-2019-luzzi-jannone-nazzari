package it.polimi.se2019.adrenalina.controller.event;

public enum EventType {
  AMMO_CARD_UPDATE_EVENT(AmmoCardUpdateEvent.class),
  DOUBLE_KILL_EVENT(DoubleKillEvent.class),
  KILLSHOT_EVENT(KillShotEvent.class),
  PLAYER_ATTACK_EVENT(PlayerAttackEvent.class),
  PLAYER_CHAT_EVENT(PlayerChatEvent.class),
  PLAYER_COLLECT_AMMO_EVENT(PlayerCollectAmmoEvent.class),
  PLAYER_COLLECT_POWERUP_EVENT(PlayerCollectPowerUpEvent.class),
  PLAYER_COLLECT_WEAPON_EVENT(PlayerCollectWeaponEvent.class),
  PLAYER_CONNECT_EVENT(PlayerConnectEvent.class),
  PLAYER_DEATH_EVENT(PlayerDeathEvent.class),
  PLAYER_MOVE_EVENT(PlayerMoveEvent.class),
  PLAYER_POWERUP_EVENT(PlayerPowerUpEvent.class),
  PLAYER_RELOAD_EVENT(PlayerReloadEvent.class),
  PLAYER_SPAWN_EVENT(PlayerSpawnEvent.class),
  SPAWN_POINT_DAMAGE_EVENT(SpawnPointDamageEvent.class),
  WEAPON_UPDATE_EVENT(WeaponUpdateEvent.class),
  TIMER_SET_EVENT(TimerSetEvent.class),
  MAP_SELECTION_EVENT(MapSelectionEvent.class),
  FINAL_FRENZY_TOGGLE_EVENT(FinalFrenzyToggleEvent.class),
  SELECT_PLAYER_EVENT(SelectPlayerEvent.class),
  SELECT_SQUARE_EVENT(SelectPlayerEvent.class);

  private final Class<? extends Event> eventClass;

  EventType(Class<? extends Event> eventClass) {
    this.eventClass = eventClass;
  }

  public Class<? extends Event> getEventClass() {
    return eventClass;
  }

  public static EventType getEventTypeByClass(Class<? extends Event> refClass) {
    for (EventType eventType : values()) {
      if (eventType.eventClass.equals(refClass)) {
        return eventType;
      }
    }
    throw new IllegalStateException("Unexpected event (" + refClass.getName() + ")");
  }
}