import type Schema from "@redinn/oceanpeace-web/api";
import goalsPlugin from "./goals";
import iconsPlugin from "./icons";
import focusPlugin from "./focus";
import mayoPlugin from "./usage";
import presetsPlugin from "./presets";
import schedulePlugin from "./schedule";

const AndroidApi: Schema = {
  ...goalsPlugin,
  ...iconsPlugin,
  ...focusPlugin,
  ...mayoPlugin,
  ...presetsPlugin,
  ...schedulePlugin,
};

export default AndroidApi;
