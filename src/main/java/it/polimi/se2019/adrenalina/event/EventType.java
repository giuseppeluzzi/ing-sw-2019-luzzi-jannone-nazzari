package it.polimi.se2019.adrenalina.event;

import it.polimi.se2019.adrenalina.event.invocations.ShowBoardInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowBuyableWeaponsInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowDeathInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowDirectionSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowEffectSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPaymentOptionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowPowerUpSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSpawnPointTrackSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowSquareSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTargetSelectInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowTurnActionSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.ShowWeaponSelectionInvocation;
import it.polimi.se2019.adrenalina.event.invocations.SwitchToFinalFrenzyInvocation;
import it.polimi.se2019.adrenalina.event.invocations.TimerSetEvent;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasAmmoCardsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardHasWeaponsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardKillShotsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.BoardStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.CurrentPlayerUpdate;
import it.polimi.se2019.adrenalina.event.modelview.DominationBoardDamagesUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.EnemyWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.OwnPowerUpUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerAmmoUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerDamagesTagsUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerKillScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerPositionUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerScoreUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerStatusUpdate;
import it.polimi.se2019.adrenalina.event.modelview.PlayerWeaponUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareAmmoCardUpdate;
import it.polimi.se2019.adrenalina.event.modelview.SquareWeaponUpdate;
import it.polimi.se2019.adrenalina.event.viewcontroller.FinalFrenzyToggleEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.MapSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerActionSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectAmmoEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerCollectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerColorSelectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerDiscardPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerNoCollectEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPaymentEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerPowerUpEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerReloadEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEffectEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSelectWeaponEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.PlayerSetColorEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectDirectionEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SelectPlayerEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SpawnPointDamageEvent;
import it.polimi.se2019.adrenalina.event.viewcontroller.SquareMoveSelectionEvent;

public enum EventType {
  FINAL_FRENZY_TOGGLE_EVENT(FinalFrenzyToggleEvent.class),
  MAP_SELECTION_EVENT(MapSelectionEvent.class),
  PLAYER_ACTION_SELECTION_EVENT(PlayerActionSelectionEvent.class),
  PLAYER_COLLECT_AMMO_EVENT(PlayerCollectAmmoEvent.class),
  PLAYER_COLLECT_WEAPON_EVENT(PlayerCollectWeaponEvent.class),
  PLAYER_COLOR_SELECTION_EVENT(PlayerColorSelectionEvent.class),
  PLAYER_CONNECT_EVENT(PlayerConnectEvent.class),
  PLAYER_DISCARD_POWERUP_EVENT(PlayerDiscardPowerUpEvent.class),
  PLAYER_NO_COLLECT_EVENT(PlayerNoCollectEvent.class),
  PLAYER_PAYMENT_EVENT(PlayerPaymentEvent.class),
  PLAYER_POWERUP_EVENT(PlayerPowerUpEvent.class),
  PLAYER_RELOAD_EVENT(PlayerReloadEvent.class),
  PLAYER_SELECT_WEAPON_EFFECT_EVENT(PlayerSelectWeaponEffectEvent.class),
  PLAYER_SELECT_WEAPON_EVENT(PlayerSelectWeaponEvent.class),
  PLAYER_SET_COLOR(PlayerSetColorEvent.class),
  SELECT_DIRECTION_EVENT(SelectDirectionEvent.class),
  SELECT_PLAYER_EVENT(SelectPlayerEvent.class),
  SELECT_SQUARE_EVENT(SelectPlayerEvent.class),
  SPAWN_POINT_DAMAGE_EVENT(SpawnPointDamageEvent.class),
  SQUARE_MOVE_SELECTION_EVENT(SquareMoveSelectionEvent.class),
  TIMER_SET_EVENT(TimerSetEvent.class),
  // Invocations:
  SHOW_BOARD_INVOCATION(ShowBoardInvocation.class),
  SHOW_TARGET_SELECT_INVOCATION(ShowTargetSelectInvocation.class),
  SHOW_DIRECTION_SELECT_INVOCATION(ShowDirectionSelectInvocation.class),
  SHOW_SQUARE_SELECT_INVOCATION(ShowSquareSelectInvocation.class),
  SHOW_BUYABLE_WEAPONS_INVOCATION(ShowBuyableWeaponsInvocation.class),
  SHOW_SPAWN_POINT_TRACK_SELECTION_INVOCATION(ShowSpawnPointTrackSelectionInvocation.class),
  SHOW_DEATH_INVOCATION(ShowDeathInvocation.class),
  SHOW_PAYMENT_OPTION_INVOCATION(ShowPaymentOptionInvocation.class),
  SHOW_WEAPON_SELECTION_INVOCATION(ShowWeaponSelectionInvocation.class),
  SHOW_EFFECT_SELECTION_INVOCATION(ShowEffectSelectionInvocation.class),
  SHOW_POWER_UP_SELECTION_INVOCATION(ShowPowerUpSelectionInvocation.class),
  SHOW_TURN_ACTION_SELECTION_INVOCATION(ShowTurnActionSelectionInvocation.class),
  SWITCH_TO_FINAL_FRENZY_INVOCATION(SwitchToFinalFrenzyInvocation.class),
  // Model View Events
  PLAYER_POSITION_UPDATE(PlayerPositionUpdate.class),
  PLAYER_DAMAGES_TAGS_UPDATE(PlayerDamagesTagsUpdate.class),
  PLAYER_SCORE_UPDATE(PlayerScoreUpdate.class),
  PLAYER_KILL_SCORE_UPDATE(PlayerKillScoreUpdate.class),
  PLAYER_STATUS_UPDATE(PlayerStatusUpdate.class),
  PLAYER_AMMO_UPDATE(PlayerAmmoUpdate.class),
  PLAYER_WEAPON_UPDATE(PlayerWeaponUpdate.class),
  ENEMY_WEAPON_UPDATE(EnemyWeaponUpdate.class),
  ENEMY_POWER_UP_UPDATE(EnemyPowerUpUpdate.class),
  OWN_POWER_UP_UPDATE(OwnPowerUpUpdate.class),
  SQUARE_AMMO_CARD_UPDATE(SquareAmmoCardUpdate.class),
  SQUARE_WEAPON_UPDATE(SquareWeaponUpdate.class),
  CURRENT_PLAYER_UPDATE(CurrentPlayerUpdate.class),
  BOARD_STATUS_UPDATE(BoardStatusUpdate.class),
  BOARD_HAS_WEAPON_UPDATE(BoardHasWeaponsUpdate.class),
  BOARD_HAS_AMMO_CARDS_UPDATE(BoardHasAmmoCardsUpdate.class),
  BOARD_KILL_SHOTS_UPDATE(BoardKillShotsUpdate.class),
  DOMINATION_BOARD_DAMAGES_UPDATE(DominationBoardDamagesUpdate.class);
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
