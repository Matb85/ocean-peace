export interface AppIconI {
  src: string;
  name: string;
}
export interface AppsUsage {
  stats: JSON;
}
export interface FocusStartedI {
  started: boolean;
}

export default interface Schema {
  getAppIcon(name: string): Promise<AppIconI>;
  getAllAppIcons(): Promise<Record<string, AppIconI>>;
  getAppsUsage(): Promise<AppsUsage>;
  startFocus(): Promise<FocusStartedI>;
}
