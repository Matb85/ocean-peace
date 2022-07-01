export interface AppsUsage {
  stats: JSON;
}
export interface FocusStartedI {
  started: boolean;
}

export interface FocusMethods {
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
}
