import { registerPlugin, Capacitor } from "@capacitor/core";
import type Schema from "../../web/api/index";
import type { AppIconI, AppsUsage } from "../../web/api/index";

interface IconsPlugin {
  getAllIcons(): Promise<{ apps: AppIconI[] }>;
  getIcon(options: { packageName: string }): Promise<{ app: AppIconI }>;
}
interface MayoPlugin {
  callMayo(): Promise<{ stats: JSON }>;
}
interface FocusPlugin {
  startContinuous(options: {
    continuousDuration: number;
    twentyRule: boolean;
    wakeDevice: boolean;
  }): Promise<{ started: boolean }>;
  startPomodoro(options: {
    workDuration: number;
    breakDuration: number;
    twentyRule: boolean;
    wakeDevice: boolean;
  }): Promise<{ started: boolean }>;
  startStopwatch(options: { twentyRule: boolean }): Promise<{ started: boolean }>;
  stopFocus();
}
interface GoalsPlugin {
  getAllGoals(): Promise<{ goals: JSON }>;
  getGoal(options: { fileName: string }): Promise<{ goal: JSON }>;
  createGoal(options: { goalName: string; apps: JSON; weekDays: string; limit: number });
  editGoal(options: { fileName: string; goalName: string; apps: JSON; weekDays: string; limit: number });
  deleteGoal(options: { fileName: string });
}

const Icons = registerPlugin<IconsPlugin>("Icons");
const Mayo = registerPlugin<MayoPlugin>("Mayo");
const Focus = registerPlugin<FocusPlugin>("Focus");
const Goal = registerPlugin<GoalsPlugin>("Goal");

const AndroidApi: Schema = {
  async getAppIcon(packageName: string): Promise<AppIconI | null> {
    try {
      const res = await Icons.getIcon({ packageName });
      res.app.iconPath = Capacitor.convertFileSrc(res.app.iconPath);
      return res.app;
    } catch (e) {
      console.error(e);
      return null;
    }
  },
  async getAppIcons(packageNames: string[]): Promise<AppIconI[]> {
    const iconsPromises: Promise<any>[] = [];
    const icons: AppIconI[] = [];
    for (const packageName of packageNames) {
      iconsPromises.push(Icons.getIcon({ packageName }).then(data => icons.push(data.app)));
    }
    await Promise.all(iconsPromises);
    return icons;
  },
  async getAllAppIcons(): Promise<AppIconI[]> {
    const val = (await Icons.getAllIcons()).apps;
    for (const key of val) {
      key.iconPath = Capacitor.convertFileSrc(key.iconPath);
    }
    return val;
  },
  async getAppsUsage(): Promise<AppsUsage> {
    const { stats } = await Mayo.callMayo();

    return { stats };
  },
  async startContinuous(continuousDuration: number, twentyRule: boolean, wakeDevice: boolean) {
    const { started } = await Focus.startContinuous({
      continuousDuration: continuousDuration,
      twentyRule: twentyRule,
      wakeDevice: wakeDevice,
    });

    return { started };
  },
  async startPomodoro(workDuration: number, breakDuration: number, twentyRule: boolean, wakeDevice: boolean) {
    const { started } = await Focus.startPomodoro({
      workDuration: workDuration,
      breakDuration: breakDuration,
      twentyRule: twentyRule,
      wakeDevice: wakeDevice,
    });

    return { started };
  },
  async startStopwatch(twentyRule: boolean) {
    const { started } = await Focus.startStopwatch({ twentyRule: twentyRule });

    return { started };
  },
  async stopFocus() {
    await Focus.stopFocus();
    return;
  },
  async getAllGoals() {
    const { goals } = await Goal.getAllGoals();
    return { goals };
  },
  async getGoal(fileName: string) {
    const { goal } = await Goal.getGoal({ fileName });
    return { goal };
  },
  async createGoal(goalName: string, apps: JSON, weekDays: string, limit: number) {
    await Goal.createGoal({ goalName, apps, weekDays, limit });
    return;
  },
  async editGoal(fileName: string, goalName: string, apps: JSON, weekDays: string, limit: number) {
    await Goal.editGoal({ fileName, goalName, apps, weekDays, limit });
    return;
  },
  async deleteGoal(fileName: string) {
    await Goal.deleteGoal({ fileName });
    return;
  },
};

export default AndroidApi;
