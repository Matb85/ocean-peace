import { AppIconI, IconsMethods } from "./icons";
import { FocusMethods } from "./focus";
import { GoalI, GoalsMethods } from "./goals";
import { PresetI, PresetsMethods } from "./presets";
import { AppsUsage, MayoMethods } from "./mayo";
import { ScheduleI, ScheduleMethods } from "./schedule";
import { UIMethods } from "./ui";
export { AppIconI, AppsUsage, GoalI, PresetI, ScheduleI };

type Schema = IconsMethods & FocusMethods & GoalsMethods & PresetsMethods & MayoMethods & ScheduleMethods & UIMethods;

export default Schema;
