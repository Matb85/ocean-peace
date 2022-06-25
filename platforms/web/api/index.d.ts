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
   * Part of Focus singleton class.
   * 
   * @param continuousDuration  Duration in milliseconds of whole focus mode
   * @param wakeDevice          Boolean if user wants the device to wake up at the end of every phase
   * @param twentyRule      Boolean if user wants to activate 20:20:20 rule
   * 
   * @return                Returns boolean if mode start was successful
   */
  startContinuous(continuousDuration: number, twentyRule: boolean, wakeDevice: boolean): Promise<FocusStartedI>;
  /**
   * Start Pomodoro mode of Focus. 
   * 
   * @param workDuration    Duration in milliseconds of work phase 
   * @param breakDuration   Duration in milliseconds of break phase
   * @param wakeDevice      Boolean if user wants the device to wake up at the end of every phase
   * @param twentyRule      Boolean if user wants to activate 20:20:20 rule
   * 
   * @return                Returns boolean if mode start was successful
   */
  startPomodoro(workDuration: number, breakDuration: number, twentyRule: boolean ,wakeDevice: boolean): Promise<FocusStartedI>;
  /**
   * Start Stopwatch mode of Focus. You don't need to provide anny additional parameters as to date. 
   * Part of Focus singleton class.
   * @param twentyRule      Boolean if user wants to activate 20:20:20 rule
   * 
   * @return                Returns boolean if mode start was successful
   */
  startStopwatch(twentyRule: boolean): Promise<FocusStartedI>;
  /**
   * Stops the running Focus.
   * Part of Focus singleton class.
   * 
   * 
   */
  stopFocus();
}
