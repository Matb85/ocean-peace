import { AppIconI, IconsMethods } from "./icons";
import { FocusMethods, AppsUsage } from "./focus";
import { GoalI, GoalsMethods } from "./goals";

export { AppIconI, GoalI, AppsUsage };

type Schema = IconsMethods & FocusMethods & GoalsMethods;

export default Schema;
