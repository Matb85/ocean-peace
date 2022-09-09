export interface AppsUsage {
  stats: JSON;
}

export interface UsageMethods {
  getAppsUsage(): Promise<AppsUsage>;
}
