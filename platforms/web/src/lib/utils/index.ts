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

/** returns a 2-element array with hours and minutes
 * @param stringTime a number
 * @returns array with the time
 */
export function timeFromNumber(stringTime: string): [number, number] {
  const time = parseInt(stringTime);
  return [Math.floor(time / 60), Math.floor(time % 60)];
}

/** returns a string with hours and minutes
 * @param stringTime a number
 * @returns string with the time
 */
export function stringTimeFromNumber(stringTime: string): string {
  const time = timeFromNumber(stringTime);
  return time[0] + ":" + time[1];
}

export const WebsiteTypes = ["single website", "all subdomains", "all domains"];
