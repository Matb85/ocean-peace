export interface AppsUsage {
  stats: JSON;
  total: number;
}

export interface UsageMethods {
  getAllUsage(): Promise<AppsUsage>;
}
