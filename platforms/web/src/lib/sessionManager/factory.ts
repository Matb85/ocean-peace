/** Creates a controller for the SM
 * @param prefix the prefix for the SM
 * @returns the controller
 */
export default function <K extends string, L extends Record<string, string>>(prefix: string) {
  return {
    setProps(data: Partial<L>) {
      for (const prop in data) {
        if (Object.prototype.hasOwnProperty.call(data, prop)) sessionStorage.setItem(prefix + prop, data[prop]);
      }
    },
    setProp(key: K, prop: string | number) {
      sessionStorage.setItem(prefix + key, prop as string);
    },
    getProp(key: K): string {
      return sessionStorage.getItem(prefix + key);
    },
    getProps<T extends K>(...keys: T[]): Partial<Record<T, string>> {
      const res: Partial<Record<T, string>> = {};
      for (const prop of keys) {
        res[prop] = sessionStorage.getItem(prefix + prop);
      }
      return res;
    },
  };
}
