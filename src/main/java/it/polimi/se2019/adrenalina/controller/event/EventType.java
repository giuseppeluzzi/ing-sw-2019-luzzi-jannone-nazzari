package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.event.view.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.controller.event.view.MapSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerNoCollectEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.controller.event.view.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.view.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.controller.event.view.SquareMoveSelectionEvent;

public enum EventType {
  AMMO_CARD_UPDATE_EVENT(AmmoCardUpdateEvent.class),
  DOUBLE_KILL_EVENT(DoubleKillEvent.class),
  FINAL_FRENZY_TOGGLE_EVENT(FinalFrenzyToggleEvent.class),
  KILLSHOT_EVENT(KillShotEvent.class),
  MAP_SELECTION_EVENT(MapSelectionEvent.class),
  PLAYER_ACTION_SELECTION_EVENT(PlayerActionSelectionEvent.class),
  PLAYER_ATTACK_EVENT(PlayerAttackEvent.class),
  PLAYER_CHAT_EVENT(PlayerChatEvent.class),
  PLAYER_COLLECT_AMMO_EVENT(PlayerCollectAmmoEvent.class),
  PLAYER_COLLECT_WEAPON_EVENT(PlayerCollectWeaponEvent.class),
  PLAYER_COLOR_SELECTION_EVENT(PlayerColorSelectionEvent.class),
  PLAYER_CONNECT_EVENT(PlayerConnectEvent.class),
  PLAYER_DEATH_EVENT(PlayerDeathEvent.class),
  PLAYER_DISCARD_POWERUP_EVENT(PlayerDiscardPowerUpEvent.class),
  PLAYER_NO_COLLECT_EVENT(PlayerNoCollectEvent.class),
  PLAYER_PAYMENT_EVENT(PlayerPaymentEvent.class),
  PLAYER_POWERUP_EVENT(PlayerPowerUpEvent.class),
  PLAYER_RELOAD_EVENT(PlayerReloadEvent.class),
  PLAYER_SELECT_WEAPON_EFFECT_EVENT(PlayerSelectWeaponEffectEvent.class),
  PLAYER_SELECT_WEAPON_EVENT(PlayerSelectWeaponEvent.class),
  PLAYER_SET_COLOR(PlayerSetColorEvent.class),
  PLAYER_SPAWN_EVENT(PlayerSpawnEvent.class),
  SELECT_DIRECTION_EVENT(SelectDirectionEvent.class),
  SELECT_PLAYER_EVENT(SelectPlayerEvent.class),
  SELECT_SQUARE_EVENT(SelectPlayerEvent.class),
  SPAWN_POINT_DAMAGE_EVENT(SpawnPointDamageEvent.class),
  SQUARE_MOVE_SELECTION_EVENT(SquareMoveSelectionEvent.class),
  TIMER_SET_EVENT(TimerSetEvent.class),
  WEAPON_UPDATE_EVENT(WeaponUpdateEvent.class),
  PLAYER_MOVE_EVENT(PlayerMoveEvent.class);

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
