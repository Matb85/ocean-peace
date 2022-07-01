export interface AppIconI {
  packageName: string;
  label: string;
  iconPath: string;
  version: string;
}

export interface IconsMethods {
  /** returns the data of all the app icons installed on the device
   * @returns all icons
   */
  getAllAppIcons(): Promise<AppIconI[]>;
  /** returns the data of specified icons
   * @returns specified icons
   */
  getAppIcons(packageNames: string[]): Promise<AppIconI[]>;
  /** returns a single app icon data object
   * @returns a single icon
   */
  getAppIcon(name: string): Promise<AppIconI | null>;
}
