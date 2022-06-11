import type { Handle } from "@sveltejs/kit";

/** disable srr
 * @returns {ReturnType<Handle>} returns the event with ssr disabled
 */
export const handle: Handle = async ({ event, resolve }) => {
  return resolve(event, { ssr: false });
};
