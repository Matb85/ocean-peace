import factory from "./factory";

export type GoalKeys = "id" | "name" | "limit" | "activeDays" | "limitActionType" | "sessionTime";
export type GoalType = Record<GoalKeys, string>;

export type PresetKeys = "id" | "name" | "icon";
export type PresetType = Record<PresetKeys, string>;

export type ScheduleKeys = "id" | "name" | "preset" | "activeDays" | "startTime" | "stopTime";
export type ScheduleType = Record<ScheduleKeys, string>;

export type DialogKeys = "apps" | "websites" | "backUrl";
export type DialogType = Record<DialogKeys, string>;

export type ActionKeys = "type" | "backUrl" | "continueUrl";
export type ActionType = Record<ActionKeys, string>;

export default {
  goal: factory<GoalKeys>("goal_"),
  preset: factory<PresetKeys>("preset_"),
  schedule: factory<ScheduleKeys>("schedule_"),
  dialogs: factory<DialogKeys>("dialog_"),
  action: factory<ActionKeys>("action_"),
};
