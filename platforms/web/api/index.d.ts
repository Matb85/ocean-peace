export interface AppIconI {
  src: string;
  name: string;
}
export interface AppsUsage {
  stats: string;
}

export default interface Schema {
  getAppIcon(name: string): Promise<AppIconI>;
  getAllAppIcons(): Promise<AppIconI[]>;
  getAppsUsage(): Promise<AppsUsage>;
}
