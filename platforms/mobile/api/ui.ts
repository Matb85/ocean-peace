import { registerPlugin } from "@capacitor/core";
import { SplashScreen } from "@capacitor/splash-screen";
import { App } from "@capacitor/app";

import type { UIMethods, PreferencesI } from "@redinn/oceanpeace-web/api/ui";
interface UIPlugin {
  fadeIn(): Promise<void>;
  fadeOut(): Promise<void>;
  getPreferences(): Promise<{ data: PreferencesI }>;
  setPreferences(data: { data: PreferencesI }): Promise<void>;
  setPreference(data: { key: keyof PreferencesI; value: string }): Promise<void>;
}

const UI = registerPlugin<UIPlugin>("UI");

const plugin: UIMethods = {
  setUpNativeBackButton(callback: () => void) {
    App.addListener("backButton", callback);
  },
  fadeIn() {
    return UI.fadeIn();
  },
  fadeOut() {
    return UI.fadeOut();
  },
  setPreferences(data: PreferencesI) {
    return UI.setPreferences({ data });
  },
  async getPreferences() {
    const data = await UI.getPreferences();
    return data.data;
  },
  setPreference(key: keyof PreferencesI, value: string) {
    return UI.setPreference({ key, value });
  },
  hideSplash() {
    return SplashScreen.hide();
  },
};

export default plugin;
