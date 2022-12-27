export interface PreferencesI {
  setupComplete: boolean;
  name: string;
  screentime: string;
}

export interface UIMethods {
  setUpNativeBackButton(callback: () => void): void;
  /** fades in the screen
   * @returns nothing
   */
  fadeIn(): Promise<void>;
  /** fades out the screen
   * @returns nothing
   */
  fadeOut(): Promise<void>;
  /** */
  setPreferences(data: PreferencesI): Promise<void>;
  getPreferences(): Promise<PreferencesI>;
  setPreference(key: string, value: string): Promise<void>;
  hideSplash(): Promise<void>;
}
