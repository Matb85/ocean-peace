import factory from "./factory";

export type GoalKeys = "id" | "name" | "limit" | "activeDays" | "limitActionType";
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
  goal: factory<GoalKeys, GoalType>("goal_"),
  preset: factory<PresetKeys, PresetType>("preset_"),
  schedule: factory<ScheduleKeys, ScheduleType>("schedule_"),
  dialogs: factory<DialogKeys, DialogType>("dialog_"),
  action: factory<ActionKeys, ActionType>("action_"),
};
