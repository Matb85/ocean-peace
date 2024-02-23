import { push } from "svelte-spa-router";
import Api from "@redinn/oceanpeace-mobile/api";

/** navigates to another page
 * @param to a string
 * @returns void
 */
export async function goTo(to: string): Promise<void> {
  await Api.fadeIn();
  push(to);
  setTimeout(() => Api.fadeOut(), 75);
}
/** [hours, minutes] */
type TimeType = [number, number];

/** returns a 2-element array with hours and minutes
 * @param stringTime a number
 * @returns [hours, minutes]
 */
export function timeFromNumber(stringTime: string): TimeType {
  const time = parseInt(stringTime);
  return [Math.floor(time / 60), Math.floor(time % 60)];
}
/** returns a 2-element array with hours and minutes
 * @param time a number
 * @returns [hours, minutes]
 */
export function numberToTime(time: number): TimeType {
  return [Math.floor(time / 60), Math.floor(time % 60)];
}
/** returns a 2-element array with hours and minutes
 * @param time a number
 * @returns [hours, minutes]
 */
export function numberFromTime(time: TimeType): number {
  return time[0] * 60 + time[1];
}

/** returns a string with hours and minutes
 * @param stringTime a number
 * @returns string with the time
 */
export function stringTimeFromNumber(stringTime: string): string {
  const time = timeFromNumber(stringTime);
  return time[0] + ":" + time[1];
}
/** returns a string with hours and minutes
 * @param minutes a number
 * @returns string with the time
 */
export function formatMinutes(minutes: number): string {
  const time = numberToTime(minutes);
  return (time[0] > 0 ? time[0] : 0) + "h " + (time[1] > 0 ? time[1] : 0) + "min";
}

export const WebsiteTypes = ["single website", "all subdomains", "all domains"];
