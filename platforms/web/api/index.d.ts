export interface AppIconI {
  packageName: string;
  label: string;
  iconPath: string;
  version: string;
}
export interface AppsUsage {
  stats: JSON;
}
export interface FocusStartedI {
  started: boolean;
}

export interface GoalI {
  id: string;
  name: string;
  apps: string;
  limit: string;
  activeDays: string;
  limitActionType: string;
}

export default interface Schema {
  /** returns the data of all the app icons installed on the device
   * @returns all icons
   */
  getAllAppIcons(): Promise<AppIconI[]>;
  /** returns the data of specified icons
   * @returns specified icons
   */
  getAppIcons(packageNames: string[]): Promise<AppIconI[]>;
  /** returns a single app icon data object
   * @returns a single icon
   */
  getAppIcon(name: string): Promise<AppIconI | null>;

  getAppsUsage(): Promise<AppsUsage>;

  // F O C U S   A P I

  /**
   * Start Continuous mode of Focus.
   * It is working only for chosen time.
   *
   * @param continuousDuration  Duration in milliseconds of whole focus mode
   * @param wakeDevice          (moblie only) Boolean if user wants the device to wake up at the end of every phase
   * @param twentyRule          Boolean if user wants to activate 20:20:20 rule
   *
   * @return                    Returns boolean if mode start was successful
   */
  startContinuous(continuousDuration: number, twentyRule: boolean, wakeDevice: boolean): Promise<FocusStartedI>;
  /**
   * Start Pomodoro mode of Focus.
   *
   * @param workDuration        Duration in milliseconds of work phase
   * @param breakDuration       Duration in milliseconds of break phase
   * @param wakeDevice          (mobile only) Boolean if user wants the device to wake up at the end of every phase
   * @param twentyRule          Boolean if user wants to activate 20:20:20 rule
   *
   * @return                    Returns boolean if mode start was successful
   */
  startPomodoro(
    workDuration: number,
    breakDuration: number,
    twentyRule: boolean,
    wakeDevice: boolean
  ): Promise<FocusStartedI>;
  /**
   * Start Stopwatch mode of Focus. You don't need to provide anny additional parameters as to date.
   *
   * @param twentyRule          Boolean if user wants to activate 20:20:20 rule
   *
   * @return                    Returns boolean if mode start was successful
   */
  startStopwatch(twentyRule: boolean): Promise<FocusStartedI>;
  /**
   * Stops the running Focus.
   *
   *
   */
  stopFocus();

  // G O A L S   A P I

  /** Returns JSON with each goal JSON as described below
   * @returns all goals
   */
  getAllGoals(): Promise<GoalI[]>;
  /** Returns goal JSON as described below
   * @param id the id of the goal
   */
  getGoal(id: string): Promise<GoalI | null>;
  /** Creates a goal and saves it to a file.
   * @param {GoalI} data data
   */
  saveGoal(data: GoalI): Promise<void>;
  /** Deletes selected goal and its file
   * @param id the id of the goal
   */
  deleteGoal(id: string): Promise<void>;
}
