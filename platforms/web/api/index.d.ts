import { AppIconI, IconsMethods } from "./icons";
import { FocusMethods, AppsUsage } from "./focus";
import { GoalI, GoalsMethods } from "./goals";
import { PresetI, PresetsMethods } from "./presets";
import { AppsUsage, MayoMethods } from "./mayo";

export { AppIconI, AppsUsage, GoalI, PresetI, AppsUsage };

type Schema = IconsMethods & FocusMethods & GoalsMethods & PresetsMethods & MayoMethods;

export default Schema;
