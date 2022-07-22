import { registerPlugin } from "@capacitor/core";
import type { AppsUsage } from "@redinn/oceanpeace-web/api";

interface MayoPlugin {
  callMayo(): Promise<{ stats: JSON }>;
  stopBackgroundMayo();
}

const Mayo = registerPlugin<MayoPlugin>("Mayo");

export default {
  async getAppsUsage(): Promise<AppsUsage> {
    const { stats } = await Mayo.callMayo();

    return { stats };
  },
  async stopBackgroundMayo() {
    await Mayo.stopBackgroundMayo();
  },
};
