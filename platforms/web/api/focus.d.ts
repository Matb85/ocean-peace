interface FocusStartedI {
  started: boolean;
}

export interface FocusMethods {
  startStopwatch(packages: string): Promise<void>;
  startContinuous(packages: string, duration: number): Promise<void>;
  startPomodoro(packages: string, workDuration: number, breakDuration: number, cyclesNumber: number): Promise <void>;
  stop(): Promise<void>;
}
