export interface PreferencesI {
  setupComplete: boolean;
  name: string;
  screentime: string;
}

export interface UIMethods {
  /** makes fades in the screen
   * @returns nothing
   */
  fadeIn(): Promise<void>;
  /** makes fades out the screen
   * @returns nothing
   */
  fadeOut(): Promise<void>;
  /** */
  setPreferences(data: PreferencesI): Promise<void>;
  getPreferences(): Promise<PreferencesI>;
  setPreference(key: string, value: string): Promise<void>;
}
