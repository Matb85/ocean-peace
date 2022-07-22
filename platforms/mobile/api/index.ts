import { registerPlugin } from "@capacitor/core";
import type Schema from "@redinn/oceanpeace-web/api";
import goalsPlugin from "./goals";
import iconsPlugin from "./icons";
import focusPlugin from "./focus";
import mayoPlugin from "./mayo";
import presetsPlugin from "./presets";

const AndroidApi: Schema = {
  ...goalsPlugin,
  ...iconsPlugin,
  ...focusPlugin,
  ...mayoPlugin,
  ...presetsPlugin,
};

export default AndroidApi;
