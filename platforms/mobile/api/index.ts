import type Schema from "@redinn/oceanpeace-web/api";

import goalsPlugin from "./goals";
import iconsPlugin from "./icons";
import focusPlugin from "./focus";
import mayoPlugin from "./mayo";

export default {
  ...goalsPlugin,
  ...iconsPlugin,
  ...focusPlugin,
  ...mayoPlugin,
} as Schema;
