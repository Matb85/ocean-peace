export interface PresetI {
  id: string;
  name: string;
  icon: string;
  apps: string;
  websites: string;
}

export interface PresetsMethods {
  /** Returns an array of all the presets
   * @returns all goals
   */
  getAllPresets(): Promise<PresetI[]>;
  /** Returns preset JSON
   * @param id the id of the goal
   */
  getPreset(id: string): Promise<PresetI | null>;
  /** Creates a preset and saves it to a file.
   * @param {PresetI} data data
   */
  savePreset(data: PresetI): Promise<void>;
  /** Deletes selected preset and its file
   * @param id the id of the goal
   */
  deletePreset(id: string): Promise<void>;
}
