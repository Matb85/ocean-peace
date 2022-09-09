import { registerPlugin } from "@capacitor/core";
import type { AppsUsage, UsageMethods as UsageMethods } from "@redinn/oceanpeace-web/api/usage";

interface UsagePlugin {
  callMayo(): Promise<{ stats: JSON, total: number }>;
}

const Usage = registerPlugin<UsagePlugin>("Usage");

const plugin: UsageMethods = {
  async getAppsUsage(): Promise<AppsUsage> {
    const { stats, total } = await Usage.callMayo();

    return { stats, total };
  },
};

export default plugin;
