import { registerPlugin } from "@capacitor/core";
import type { AppsUsage, UsageMethods as UsageMethods } from "@redinn/oceanpeace-web/api/usage";

interface UsagePlugin {
  getAllUsage(): Promise<{ stats: JSON, total: number }>;
}

const Usage = registerPlugin<UsagePlugin>("Usage");

const plugin: UsageMethods = {
  async getAllUsage(): Promise<AppsUsage> {
    const { stats, total } = await Usage.getAllUsage();

    return { stats, total };
  },
};

export default plugin;
