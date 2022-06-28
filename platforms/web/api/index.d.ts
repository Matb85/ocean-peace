export interface AppIconI {
  src: string;
  name: string;
}
export interface AppsUsage {
  stats: JSON;
}
export interface FocusStartedI {
  started: boolean;
}

export default interface Schema {
  getAppIcon(name: string): Promise<AppIconI>;
  getAllAppIcons(): Promise<AppIconI[]>;
  getAppsUsage(): Promise<AppsUsage>;
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
  startPomodoro(workDuration: number, breakDuration: number, twentyRule: boolean ,wakeDevice: boolean): Promise<FocusStartedI>;
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
  /**
   * Returns JSON with each goal JSON as described below
   * 
   * @return                    JSON: "1": ("name", "apps" (JSON), "weekDays" (JSON), "limit" (number)), ...
   */
  getAllGoals(): Promise<{ goals: JSON }>;
  /**
   * Returns goal JSON as described below
   * 
   * @param fileName            goal fileName "1.properties"
   * 
   * @return                    JSON: "name", "apps" (JSON), "weekDays" (JSON), "limit" (number)
   */
  getGoal(fileName: string): Promise<{ goal: JSON }>;
  /**
   * Creates a goal and save it's to file.
   * 
   * @param goalName            goal's title as string
   * @param apps                JSON of selected apps
   * @param weekDays            JSON of selected days ("true", "false", ....)
   * @param limit               daily time limit as number
   */
  createGoal(goalName: string, apps: JSON, weekDays: JSON, limit: number);
  /**
   * Updates and saves data to existing goal's file
   * 
   * @param fileName            file name of the goal
   * @param goalName            the goal title
   * @param apps                JSON of selected apps
   * @param weekDays            JSON of selected days ("true", "false", ....)
   * @param limit               daily time limit as number
   */
  editGoal(fileName: string, goalName: string, apps: JSON, weekDays: JSON, limit: number);
  /**
   * Deletes selected goal and it's file
   * 
   * @param fileName            file name of the goal
   */
  deleteGoal(fileName: string);
}
