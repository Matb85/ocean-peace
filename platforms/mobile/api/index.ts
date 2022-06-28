import { registerPlugin, Capacitor } from "@capacitor/core";
import type Schema from "../../web/api/index";
import type { AppIconI, AppsUsage } from "../../web/api/index";

interface IconsPlugin {
  getAllIcons(): Promise<{ apps: AppIconI[] }>;
}
interface MayoPlugin {
  callMayo(): Promise<{ stats: JSON }>;
}
interface FocusPlugin {
  startContinuous(): Promise<{ started: boolean }>;
}

const Icons = registerPlugin<IconsPlugin>("Icons");
const Mayo = registerPlugin<MayoPlugin>("Mayo");
const Focus = registerPlugin<FocusPlugin>("Focus");

const AndroidApi: Schema = {
  async getAppIcon(name: string): Promise<AppIconI> {
    return new Promise(resolve => resolve({ src: name, name }));
  },
  async getAllAppIcons(): Promise<AppIconI[]> {
    const val = (await Icons.getAllIcons()).apps;
    for (const key of val) {
      key.src = Capacitor.convertFileSrc(key.src);
    }
    console.log("watchout", val);

    return val;
  },
  async getAppsUsage(): Promise<AppsUsage> {
    const { stats } = await Mayo.callMayo();

    return { stats };
  },
  async startFocus() {
    const { started } = await Focus.startContinuous();

    return { started };
  },
};

export default AndroidApi;
