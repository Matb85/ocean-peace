export interface AppsUsage {
  stats: JSON;
  total: number;
}

export interface UsageMethods {
  getAppsUsage(): Promise<AppsUsage>;
}
