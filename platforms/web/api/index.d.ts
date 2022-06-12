export interface AppIconI {
  src: string;
  name: string;
}

export default interface Schema {
  getAppIcon(name: string): Promise<AppIconI>;
  getAllAppIcons(): Promise<AppIconI[]>;
}
