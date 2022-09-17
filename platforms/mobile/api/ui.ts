import { registerPlugin, Capacitor } from "@capacitor/core";
import type { UIMethods } from "@redinn/oceanpeace-web/api/ui";

interface UIPlugin {
  fadeIn(): Promise<void>;
  fadeOut(): Promise<void>;
}

const UI = registerPlugin<UIPlugin>("UI");

const plugin: UIMethods = {
  fadeIn() {
    return UI.fadeIn();
  },
  fadeOut() {
    return UI.fadeOut();
  },
};

export default plugin;
