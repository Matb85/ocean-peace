import { registerPlugin } from "@capacitor/core";
import type { UIMethods, PreferencesI } from "@redinn/oceanpeace-web/api/ui";
import { SplashScreen } from "@capacitor/splash-screen";
interface UIPlugin {
  fadeIn(): Promise<void>;
  fadeOut(): Promise<void>;
  getPreferences(): Promise<{ data: PreferencesI }>;
  setPreferences(data: { data: PreferencesI }): Promise<void>;
  setPreference(data: { key: keyof PreferencesI; value: string }): Promise<void>;
}

const UI = registerPlugin<UIPlugin>("UI");

const plugin: UIMethods = {
  fadeIn() {
    return UI.fadeIn();
  },
  fadeOut() {
    return UI.fadeOut();
  },
  setPreferences(data: PreferencesI) {
    return UI.setPreferences({ data });
  },
  getPreferences() {
    return UI.getPreferences().then(data => data.data);
  },
  setPreference(key: keyof PreferencesI, value: string) {
    return UI.setPreference({ key, value });
  },
  hideSplash() {
    return SplashScreen.hide();
  },
};

export default plugin;
