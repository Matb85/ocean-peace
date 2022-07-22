import { registerPlugin, Capacitor } from "@capacitor/core";
import type { AppIconI, IconsMethods } from "@redinn/oceanpeace-web/api/icons";

interface IconsPlugin {
  getAllIcons(): Promise<{ apps: AppIconI[] }>;
  getIcon(options: { packageName: string }): Promise<{ app: AppIconI }>;
}

const Icons = registerPlugin<IconsPlugin>("Icons");

const plugin: IconsMethods = {
  async getAppIcon(packageName: string): Promise<AppIconI | null> {
    try {
      const res = await Icons.getIcon({ packageName });
      res.app.iconPath = Capacitor.convertFileSrc(res.app.iconPath);
      return res.app;
    } catch (e) {
      console.error(e);
      return null;
    }
  },
  async getAppIcons(packageNames: string[]): Promise<AppIconI[]> {
    const iconsPromises: Promise<any>[] = [];
    const icons: AppIconI[] = [];
    for (const packageName of packageNames) {
      iconsPromises.push(
        Icons.getIcon({ packageName }).then(({ app }) => {
          app.iconPath = Capacitor.convertFileSrc(app.iconPath);
          icons.push(app);
        })
      );
    }
    await Promise.all(iconsPromises);
    return icons;
  },
  async getAllAppIcons(): Promise<AppIconI[]> {
    const val = (await Icons.getAllIcons()).apps;
    for (const key of val) {
      key.iconPath = Capacitor.convertFileSrc(key.iconPath);
    }
    return val;
  },
};

export default plugin;
