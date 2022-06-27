import { registerPlugin, Capacitor } from "@capacitor/core";
import Schema, { AppIconI, AppsUsage, FocusStartedI } from "../../web/api/index";

interface IconsPlugin {
  getAllIcons(): Promise<Record<string, AppIconI>>;
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
  async getAllAppIcons(): Promise<Record<string, AppIconI>> {
    const val = await Icons.getAllIcons();
    for (const key in val) {
      val[key].src = Capacitor.convertFileSrc(val[key].src);
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
