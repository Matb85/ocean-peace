import { registerPlugin } from "@capacitor/core";
import type { AppsUsage, UsageMethods as UsageMethods } from "@redinn/oceanpeace-web/api/usage";

interface UsagePlugin {
  callMayo(): Promise<{ stats: JSON }>;
}

const Usage = registerPlugin<UsagePlugin>("Usage");

const plugin: UsageMethods = {
  async getAppsUsage(): Promise<AppsUsage> {
    const { stats } = await Usage.callMayo();

    return { stats };
  },
};

export default plugin;
