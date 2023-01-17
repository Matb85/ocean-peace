import { AppIconI } from "./icons";

export interface AppsUsage {
  stats: JSON;
  totalTime: number;
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
  /** used in insights.svelte in the chart where apps used today are displayed
   * @returns an array with 3 most used a a 4th element that represents the rest
   */
  getAppsUsedToday(): Promise<SingleAppUsageI[]>;
  /** used in insights.svelte in the usage intensity chart
   * returns the number of phone unlocks per hour for the last 7 hours
   * @returns a 10-element array, each element contains:
   * the hour - time of the day
   * the x position on the chart (just so it fits)
   * the y position (the number of phone unlocks during that hour)
   */
  getUsageIntensityToday(): Promise<HourlyUsageI[]>;
  /** used in insights.svelte in the screen time 7-day history
   * returns an array with screen time stats from the last 7 days
   * @returns a 7-element array, each element contains:
   * the name of the day
   * the number of minutes the phone was used for on that day
   * boolean whether that day was successful or not (the goal screen time was exceeded or not)
   */
  getScreenTimeHistory(): Promise<GoalHistoryI[]>;
}
