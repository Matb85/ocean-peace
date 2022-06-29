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

export default interface Schema {
  getAppIcon(name: string): Promise<AppIconI | null>;
  getAppIcons(packageNames: string[]): Promise<AppIconI[]>;
  getAllAppIcons(): Promise<AppIconI[]>;
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

  /**
   * Returns JSON with each goal JSON as described below
   *
   * @return                    "0.properties": {"name": "example", "apps": {"1": "com.example", ...}, "weekDays": "1010101", "limit": (number), "history": "0101000...."}, ...
   */
  getAllGoals(): Promise<{ goals: JSON }>;
  /**
   * Returns goal JSON as described below
   *
   * @param fileName            goal fileName "1.properties"
   *
   * @return                    {"name": "example", "apps": {"1": "com.example", ...}, "weekDays": "1010101", "limit": (number), "history": "0101000...."}
   */
  getGoal(fileName: string): Promise<{ goal: JSON }>;
  /**
   * Creates a goal and save it's to file.
   *
   * @param goalName            goal's title as string
   * @param apps                JSON of selected apps {"1": "com.example", ...}
   * @param weekDays            string of 1/0 values ("1001001")
   * @param limit               daily time limit as number
   */
  createGoal(goalName: string, apps: JSON, weekDays: string, limit: number);
  /**
   * Updates and saves data to existing goal's file
   *
   * @param fileName            file name of the goal
   * @param goalName            the goal title
   * @param apps                JSON of selected apps
   * @param weekDays            string of 1/0 values ("1001001")
   * @param limit               daily time limit as number
   */
  editGoal(fileName: string, goalName: string, apps: JSON, weekDays: string, limit: number);
  /**
   * Deletes selected goal and it's file
   *
   * @param fileName            file name of the goal
   */
  deleteGoal(fileName: string);
}
