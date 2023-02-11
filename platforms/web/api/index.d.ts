import { AppIconI, IconsMethods } from "./icons";
import { FocusMethods } from "./focus";
import { GoalI, GoalsMethods } from "./goals";
import { PresetI, PresetsMethods } from "./presets";
import { AppsUsage, SingleAppUsageI, GoalHistoryI, HourlyUsageI, UsageMethods } from "./usage";
import { ScheduleI, ScheduleMethods } from "./schedule";
import { UIMethods } from "./ui";
import { PermissionsI, type PermissionsMethods } from "./permissions";
export { AppIconI, AppsUsage, GoalHistoryI, HourlyUsageI, SingleAppUsageI, GoalI, PresetI, ScheduleI };

type Schema = IconsMethods &
  FocusMethods &
  GoalsMethods &
  PresetsMethods &
  UsageMethods &
  ScheduleMethods &
  UIMethods &
  PermissionsMethods;

export default Schema;
