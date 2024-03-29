package it.polimi.se2019.adrenalina.event;

import it.polimi.se2019.adrenalina.event.invocations.*;
import it.polimi.se2019.adrenalina.event.modelview.*;
import it.polimi.se2019.adrenalina.event.viewcontroller.*;

public enum EventType {
  FINAL_FRENZY_TOGGLE_EVENT(FinalFrenzyToggleEvent.class),
  MAP_SELECTION_EVENT(MapSelectionEvent.class),
  PLAYER_ACTION_SELECTION_EVENT(PlayerActionSelectionEvent.class),
  PLAYER_COLLECT_AMMO_EVENT(PlayerCollectAmmoEvent.class),
  PLAYER_COLLECT_WEAPON_EVENT(PlayerCollectWeaponEvent.class),
  PLAYER_COLOR_SELECTION_EVENT(PlayerColorSelectionEvent.class),
  PLAYER_CONNECT_EVENT(PlayerConnectEvent.class),
  PLAYER_DISCONNECT_EVENT(PlayerDisconnectEvent.class),
  PLAYER_DISCARD_POWERUP_EVENT(PlayerDiscardPowerUpEvent.class),
  PLAYER_PAYMENT_EVENT(PlayerPaymentEvent.class),
  PLAYER_POWERUP_EVENT(PlayerPowerUpEvent.class),
  PLAYER_RELOAD_EVENT(PlayerReloadEvent.class),
  PLAYER_SELECT_WEAPON_EFFECT_EVENT(PlayerSelectWeaponEffectEvent.class),
  PLAYER_SWAP_WEAPON_EVENT(PlayerSwapWeaponEvent.class),
  PLAYER_SELECT_WEAPON_EVENT(PlayerSelectWeaponEvent.class),
  PLAYER_SET_COLOR(PlayerSetColorEvent.class),
  SELECT_DIRECTION_EVENT(SelectDirectionEvent.class),
  SELECT_PLAYER_EVENT(SelectPlayerEvent.class),
  SKIP_SELECTION_EVENT(SkipSelectionEvent.class),
  SELECT_SQUARE_EVENT(SelectSquareEvent.class),
  SPAWN_POINT_DAMAGE_EVENT(SpawnPointDamageEvent.class),
  SQUARE_MOVE_SELECTION_EVENT(SquareMoveSelectionEvent.class),
  TIMER_SET_EVENT(TimerSetEvent.class),
  PLAYER_UNSUSPEND_EVENT(PlayerUnsuspendEvent.class),
  // Invocations
  SHOW_BOARD_INVOCATION(ShowBoardInvocation.class),
  SHOW_MESSAGE_INVOCATION(ShowMessageInvocation.class),
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
  SHOW_SWAP_WEAPON_SELECTION_INVOCATION(ShowSwapWeaponSelectionInvocation.class),
  SHOW_FINAL_RANKS_INVOCATION(ShowFinalRanksInvocation.class),
  SHOW_RELOAD_WEAPON_SELECTION_INVOCATION(ShowReloadWeaponSelectionInvocation.class),
  SHOW_UNSUSPEND_PROMPT_INVOCATION(ShowUnsuspendPromptInvocation.class),
  // Model View Events
  PLAYER_MASTER_UPDATE(PlayerMasterUpdate.class),
  PLAYER_POSITION_UPDATE(PlayerPositionUpdate.class),
  PLAYER_FRENZY_UPDATE(PlayerFrenzyUpdate.class),
  PLAYER_DAMAGES_TAGS_UPDATE(PlayerDamagesTagsUpdate.class),
  PLAYER_SCORE_UPDATE(PlayerScoreUpdate.class),
  PLAYER_KILL_SCORE_UPDATE(PlayerKillScoreUpdate.class),
  PLAYER_STATUS_UPDATE(PlayerStatusUpdate.class),
  PLAYER_AMMO_UPDATE(PlayerAmmoUpdate.class),
  PLAYER_WEAPON_UPDATE(OwnWeaponUpdate.class),
  ENEMY_WEAPON_UPDATE(EnemyWeaponUpdate.class),
  ENEMY_POWER_UP_UPDATE(EnemyPowerUpUpdate.class),
  OWN_POWER_UP_UPDATE(OwnPowerUpUpdate.class),
  SQUARE_AMMO_CARD_UPDATE(SquareAmmoCardUpdate.class),
  SQUARE_WEAPON_UPDATE(SquareWeaponUpdate.class),
  CURRENT_PLAYER_UPDATE(CurrentPlayerUpdate.class),
  BOARD_STATUS_UPDATE(BoardStatusUpdate.class),
  BOARD_SKULLS_UPDATE(BoardSkullsUpdate.class),
  BOARD_HAS_WEAPON_UPDATE(BoardHasWeaponsUpdate.class),
  BOARD_HAS_AMMO_CARDS_UPDATE(BoardHasAmmoCardsUpdate.class),
  BOARD_KILL_SHOTS_UPDATE(BoardKillShotsUpdate.class),
  DOMINATION_BOARD_DAMAGES_UPDATE(DominationBoardDamagesUpdate.class),
  PLAYER_DEATH_UPDATE(PlayerDeathUpdate.class),
  BOARD_SET_SQUARE_UPDATE(BoardSetSquareUpdate.class),
  BOARD_ADD_PLAYER_UPDATE(BoardAddPlayerUpdate.class),
  BOARD_REMOVE_PLAYER_UPDATE(BoardRemovePlayerUpdate.class),
  PING_EVENT(PingEvent.class),
  CONFIGURATION_UPDATE(ConfigurationUpdate.class);

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
