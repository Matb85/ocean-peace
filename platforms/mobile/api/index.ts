import { registerPlugin } from "@capacitor/core";
import Schema, { AppIconI, AppsUsage, FocusStartedI } from "../../web/api/index";

interface EchoPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
interface MayoPlugin {
  callMayo(): Promise<{ stats: JSON }>;
}
interface FocusPlugin {
  startContinuous(): Promise<{ started: boolean }>;
}

const Echo = registerPlugin<EchoPlugin>("Echo");
const Mayo = registerPlugin<MayoPlugin>("Mayo");
const Focus = registerPlugin<FocusPlugin>("Focus");


const AndroidApi: Schema = {
  async getAppIcon(name: string): Promise<AppIconI> {
    const { value } = await Echo.echo({ value: name });

    console.log("Response from native:", value);
    return { src: value, name };
  },
  async getAllAppIcons(): Promise<AppIconI[]> {
    const name = "facebook";
    const { value } = await Echo.echo({ value: name });

    console.log("Response from native:", value);
    return [{ src: value, name }];
  },
  async getAppsUsage(): Promise<AppsUsage> {
    const { stats } = await Mayo.callMayo();

    return { stats };
  },
  async startFocus() {
    const { started } = await Focus.startContinuous();

    return { started };
  }
};

export default AndroidApi;
