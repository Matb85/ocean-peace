import type Schema from "@redinn/oceanpeace-web/api";
import goalsPlugin from "./goals";
import iconsPlugin from "./icons";
import focusPlugin from "./focus";
import usagePlugin from "./usage";
import presetsPlugin from "./presets";
import schedulePlugin from "./schedule";
import uiPlugin from "./ui";
import permissionsPlugin from "./permissions";

const AndroidApi: Schema = {
  ...goalsPlugin,
  ...iconsPlugin,
  ...focusPlugin,
  ...usagePlugin,
  ...presetsPlugin,
  ...schedulePlugin,
  ...uiPlugin,
  ...permissionsPlugin,
};

export default AndroidApi;
