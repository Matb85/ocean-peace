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
  /** used in insights.svelte
   * @returns a 10-element array, each element contains:
   * the hour - time of the day
   * the x position on the chart (just so it fits)
   * the y position (the number of phone unlocks during that hour)
   */
  getHourlyUsageToday(): Promise<HourlyUsageI[]>;
  /** used in insights.svelte
   * @returns a 7-element array, each element contains:
   * the name of the day
   * the number of minutes the phone was used for on that day
   * boolean whether that day was successful or not (the goal screen time was exceeded or not)
   */
  getScreenTimeHistory(): Promise<GoalHistoryI[]>;
}
