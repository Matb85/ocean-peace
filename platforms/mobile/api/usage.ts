import type { AppIconI } from "$schema";
import { Capacitor, registerPlugin } from "@capacitor/core";
import type {
  AppsUsage,
  GoalHistoryI,
  SingleAppUsageI,
  HourlyUsageI,
  UsageMethods,
} from "@redinn/oceanpeace-web/api/usage";

interface UsagePlugin {
  getAppsUsageToday(): Promise<{ data: SingleAppUsageI[] }>;
  getTotalTime(): Promise<{ data: number }>;
  getUnlocks(): Promise<{ data: HourlyUsageI[] }>;
}

const Usage = registerPlugin<UsagePlugin>("Usage");

const plugin: UsageMethods = {
  getTotalTime: () => Usage.getTotalTime().then(x => x.data),

  async getAppsUsedToday(): Promise<SingleAppUsageI[]> {
    const { data: stats } = await Usage.getAppsUsageToday();
    let colors: string[] = ["#B5179E", "#3772FF", "#FCBA04", "#F8F5FA"];
    //console.log("STATS READY");
    //console.log(JSON.stringify(stats, null, 2));
    for (let i = 0; i < Math.min(4, stats.length); i++) {
      stats[i].icon.iconPath = Capacitor.convertFileSrc(stats[i].icon.iconPath);
      stats[i].color = colors[i];
    }

    return stats;
    /* [
      { minutes: 70, color: "#3772FF", icon: { packageName: "", label: "Instagram", iconPath: "", version: "" } },
      { minutes: 100, color: "#FCBA04", icon: { packageName: "", label: "Facebook", iconPath: "", version: "" } },
      { minutes: 100, color: "#F8F5FA", icon: { packageName: "", label: "Rest", iconPath: "", version: "" } },
    ];
    */
  },

  getUsageIntensityToday: () => Usage.getUnlocks().then(x => x.data),
  /*
    [
      { hour: "8am", key: 0, value: 0 },
      { hour: "9am", key: 10, value: 30 },
      { hour: "10am", key: 20, value: 45 },
      { hour: "11am", key: 30, value: 40 },
      { hour: "12am", key: 40, value: 80 },
      { hour: "1pm", key: 50, value: 50 },
      { hour: "2pm", key: 60, value: 40 },
      { hour: "3pm", key: 70, value: 90 },
      { hour: "4pm", key: 80, value: 60 },
      { hour: "5pm", key: 90, value: 80 },
      { hour: "6pm", key: 100, value: 100 },
    ]; 
    */

  async getScreenTimeHistory(): Promise<GoalHistoryI[]> {
    return [
      { day: "yesterday", status: true, minutes: 200 },
      { day: "Fri", status: true, minutes: 200 },
      { day: "Thu", status: true, minutes: 200 },
      { day: "Wed", status: false, minutes: 200 },
      { day: "Tue", status: false, minutes: 200 },
      { day: "Mon", status: true, minutes: 200 },
      { day: "Sun", status: false, minutes: 200 },
    ];
  },
};

export default plugin;
