import { registerPlugin } from "@capacitor/core";
import type { FocusMethods } from "@redinn/oceanpeace-web/api/focus";
interface FocusPlugin {
  startStopwatch(data: { packages: string}): Promise<void>;
  startContinuous(data: { duration: number, packages: string, }): Promise<void>;
  startPomodoro(data: {packages: string, workDuration: number, breakDuration: number, cyclesNumber: number}): Promise <void>;
  stop(): Promise<void>;
}

const Focus = registerPlugin<FocusPlugin>("Focus");

const plugin: FocusMethods = {
  async startStopwatch(packages: string): Promise<void> {
    return Focus.startStopwatch({packages});
  },
  async startContinuous(packages: string, duration: number): Promise<void> {
    return Focus.startContinuous( {packages, duration});
  },
  startPomodoro(packages: string, workDuration: number, breakDuration: number, cyclesNumber: number): Promise <void> {
    return Focus.startPomodoro({packages, workDuration, breakDuration, cyclesNumber});
  },
  stop(): Promise<void> {
    return Focus.stop();
  }
};

export default plugin;
