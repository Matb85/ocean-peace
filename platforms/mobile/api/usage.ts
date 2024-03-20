import { Capacitor, registerPlugin } from "@capacitor/core";
import type { GoalHistoryI, SingleAppUsageI, HourlyUsageI, UsageMethods } from "@redinn/oceanpeace-web/api/usage";

interface UsagePlugin {
  getAppsUsageToday(): Promise<{ data: SingleAppUsageI[] }>;
  getTotalTime(): Promise<{ data: number }>;
  getUnlocks(): Promise<{ data: HourlyUsageI[] }>;
  getScreenTimeHistory(): Promise<{ data: GoalHistoryI[] }>;
}

const Usage = registerPlugin<UsagePlugin>("Usage");

const plugin: UsageMethods = {
  getTotalTime: () => Usage.getTotalTime().then(x => x.data),

  async getAppsUsedToday(): Promise<SingleAppUsageI[]> {
    const { data: stats } = await Usage.getAppsUsageToday();
    let colors: string[] = ["#B5179E", "#3772FF", "#FCBA04", "#F8F5FA"];
    console.log("STATS READY");
    stats.sort((a, b) => b.minutes - a.minutes);
    stats.splice(Math.min(4, stats.length));
    console.log(JSON.stringify(stats, null, 2));

    for (let i = 0; i < Math.min(4, stats.length); i++) {
      stats[i].icon.iconPath = Capacitor.convertFileSrc(stats[i].icon.iconPath);
      stats[i].color = colors[i];
    }

    return stats;
  },

  getUsageIntensityToday: () => Usage.getUnlocks().then(x => x.data),
  getScreenTimeHistory: () => Usage.getScreenTimeHistory().then(x => x.data),
};

export default plugin;
