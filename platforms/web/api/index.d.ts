import { AppIconI, IconsMethods } from "./icons";
import { FocusMethods } from "./focus";
import { GoalI, GoalsMethods } from "./goals";
import { PresetI, PresetsMethods } from "./presets";
import { AppsUsage, UsageMethods } from "./usage";
import { ScheduleI, ScheduleMethods } from "./schedule";

export { AppIconI, AppsUsage, GoalI, PresetI, ScheduleI };

type Schema = IconsMethods & FocusMethods & GoalsMethods & PresetsMethods & UsageMethods & ScheduleMethods;

export default Schema;
