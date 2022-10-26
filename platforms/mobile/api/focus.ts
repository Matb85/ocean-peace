import { registerPlugin } from "@capacitor/core";
import type { FocusMethods } from "@redinn/oceanpeace-web/api/focus";
interface FocusPlugin {
}

const Focus = registerPlugin<FocusPlugin>("Focus");

const plugin: FocusMethods = {
  
};

export default plugin;
