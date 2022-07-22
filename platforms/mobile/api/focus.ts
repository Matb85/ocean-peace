import { registerPlugin } from "@capacitor/core";

interface FocusPlugin {
  startContinuous(options: {
    continuousDuration: number;
    twentyRule: boolean;
    wakeDevice: boolean;
  }): Promise<{ started: boolean }>;
  startPomodoro(options: {
    workDuration: number;
    breakDuration: number;
    twentyRule: boolean;
    wakeDevice: boolean;
  }): Promise<{ started: boolean }>;
  startStopwatch(options: { twentyRule: boolean }): Promise<{ started: boolean }>;
  stopFocus();
}

const Focus = registerPlugin<FocusPlugin>("Focus");

export default {
  async startContinuous(continuousDuration: number, twentyRule: boolean, wakeDevice: boolean) {
    const { started } = await Focus.startContinuous({
      continuousDuration: continuousDuration,
      twentyRule: twentyRule,
      wakeDevice: wakeDevice,
    });

    return { started };
  },
  async startPomodoro(workDuration: number, breakDuration: number, twentyRule: boolean, wakeDevice: boolean) {
    const { started } = await Focus.startPomodoro({
      workDuration: workDuration,
      breakDuration: breakDuration,
      twentyRule: twentyRule,
      wakeDevice: wakeDevice,
    });

    return { started };
  },
  async startStopwatch(twentyRule: boolean) {
    const { started } = await Focus.startStopwatch({ twentyRule: twentyRule });

    return { started };
  },
  async stopFocus() {
    await Focus.stopFocus();
    return;
  },
};
