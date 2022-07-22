export interface AppsUsage {
  stats: JSON;
}

export interface MayoMethods {
  getAppsUsage(): Promise<AppsUsage>;
  /**
   * Stops running Mayo in background
   */
  stopBackgroundMayo();
}
