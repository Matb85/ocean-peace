import { registerPlugin } from "@capacitor/core";
import type { AppsUsage, MayoMethods } from "@redinn/oceanpeace-web/api/mayo";

interface MayoPlugin {
  callMayo(): Promise<{ stats: JSON }>;
  stopBackgroundMayo();
}

const Mayo = registerPlugin<MayoPlugin>("Mayo");

const plugin: MayoMethods = {
  async getAppsUsage(): Promise<AppsUsage> {
    const { stats } = await Mayo.callMayo();

    return { stats };
  },
  async stopBackgroundMayo() {
    await Mayo.stopBackgroundMayo();
  },
};

export default plugin;
