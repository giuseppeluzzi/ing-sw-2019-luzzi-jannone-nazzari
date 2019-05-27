package it.polimi.se2019.adrenalina.controller.event;

import it.polimi.se2019.adrenalina.controller.event.invocations.ShowBoardInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowBuyableWeaponsInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowDeath;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowDirectionSelectInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowEffectSelectionInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowPaymentOptionInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowPowerUpSelectionInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowSpawnPointTrackSelectionInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowSquareSelectInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowTargetSelectInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowTurnActionSelectionInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.ShowWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.SwitchToFinalFrenzyInvocation;
import it.polimi.se2019.adrenalina.controller.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerNoCollectEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.controller.event.viewcontroller.SquareMoveSelectionEvent;

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
  PLAYER_MOVE_EVENT(PlayerMoveEvent.class),
  // Invocations:
  SHOW_BOARD_INVOCATION(ShowBoardInvocation.class),
  SHOW_TARGET_SELECT_INVOCATION(ShowTargetSelectInvocation.class),
  SHOW_DIRECTION_SELECT_INVOCATION(ShowDirectionSelectInvocation.class),
  SHOW_SQUARE_SELECT_INVOCATION(ShowSquareSelectInvocation.class),
  SHOW_BUYABLE_WEAPONS_INVOCATION(ShowBuyableWeaponsInvocation.class),
  SHOW_SPAWN_POINT_TRACK_SELECTION_INVOCATION(ShowSpawnPointTrackSelectionInvocation.class),
  SHOW_DEATH_INVOCATION(ShowDeath.class),
  SHOW_PAYMENT_OPTION_INVOCATION(ShowPaymentOptionInvocation.class),
  SHOW_WEAPON_SELECTION_INVOCATION(ShowWeaponSelectionInvocation.class),
  SHOW_EFFECT_SELECTION_INVOCATION(ShowEffectSelectionInvocation.class),
  SHOW_POWER_UP_SELECTION_INVOCATION(ShowPowerUpSelectionInvocation.class),
  SHOW_TURN_ACTION_SELECTION_INVOCATION(ShowTurnActionSelectionInvocation.class),
  SWITCH_TO_FINAL_FRENZY_INVOCATION(SwitchToFinalFrenzyInvocation.class);
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
