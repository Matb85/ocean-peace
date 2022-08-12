export interface ScheduleI {
  id: string;
  name: string;
  preset: string;
  activeDays: string;
  startTime: string;
  stopTime: string;
}

export interface ScheduleMethods {
  /** Returns an array of all the presets
   * @returns all goals
   */
  getAllSchedules(): Promise<ScheduleI[]>;
  /** Returns preset JSON
   * @param id the id of the goal
   */
  getSchedule(id: string): Promise<ScheduleI | null>;
  /** Creates a preset and saves it to a file.
   * @param {ScheduleI} data data
   */
  saveSchedule(data: ScheduleI): Promise<void>;
  /** Deletes selected preset and its file
   * @param id the id of the goal
   */
  deleteSchedule(id: string): Promise<void>;
}
