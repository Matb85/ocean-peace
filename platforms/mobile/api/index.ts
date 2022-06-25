import { registerPlugin } from "@capacitor/core";
import Schema, { AppIconI, AppsUsage, FocusStartedI } from "../../web/api/index";

interface EchoPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
interface MayoPlugin {
  callMayo(): Promise<{ stats: JSON }>;
}
interface FocusPlugin {
  startContinuous(options: { continuousDuration: number, twentyRule: boolean, wakeDevice: boolean }): Promise<{ started: boolean }>;
  startPomodoro(options: { workDuration: number, breakDuration: number, twentyRule: boolean, wakeDevice: boolean }): Promise<{ started: boolean }>;
  startStopwatch(options: { twentyRule: boolean }): Promise<{ started: boolean }>;
  stopFocus();
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
  async startContinuous(continuousDuration: number, twentyRule: boolean, wakeDevice: boolean) {
    const { started } = await Focus.startContinuous({ continuousDuration: continuousDuration, twentyRule: twentyRule, wakeDevice: wakeDevice });

    return { started };
  },
  async startPomodoro(workDuration: number, breakDuration: number, twentyRule: boolean, wakeDevice: boolean) {
    const { started } = await Focus.startPomodoro({ workDuration: workDuration, breakDuration: breakDuration, twentyRule: twentyRule, wakeDevice: wakeDevice });

    return { started };
  },
  async startStopwatch(twentyRule: boolean) {
    const { started } = await Focus.startStopwatch({twentyRule: twentyRule});

    return { started };
  },
  async stopFocus() {
    await Focus.stopFocus();
    return;
  }
};

export default AndroidApi;
