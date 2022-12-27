import { AppIconI } from "./icons";

export interface AppsUsage {
  stats: JSON;
  total: number;
}
export interface SingleAppUsageI {
  color: string;
  minutes: number;
  icon: AppIconI;
}
export interface GoalHistoryI {
  day: string;
  status: boolean;
  minutes: number;
}
export interface HourlyUsageI {
  hour: string;
  key: number;
  value: number;
}

export interface UsageMethods {
  getAppsUsage(): Promise<AppsUsage>;
  /** used in insights.svelte
   * @returns an array with 3 most used a a 4th element that represents the rest
   */
  getAppsUsedToday(): Promise<SingleAppUsageI[]>;
  getHourlyUsageToday(): Promise<HourlyUsageI[]>;
  getScreenTimeHistory(): Promise<GoalHistoryI[]>;
}
