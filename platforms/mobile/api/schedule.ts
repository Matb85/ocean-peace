import { registerPlugin } from "@capacitor/core";
import type { ScheduleI, ScheduleMethods } from "@redinn/oceanpeace-web/api/schedule";

interface SchedulesPlugin {
  getAllSchedules(): Promise<{ schedules: ScheduleI[] }>;
  getSchedule(data: { id: string }): Promise<{ schedule: ScheduleI }>;
  saveSchedule(data: { data: ScheduleI }): void;
  deleteSchedule(data: { id: string }): void;
}

const Schedule = registerPlugin<SchedulesPlugin>("Schedule");

const plugin: ScheduleMethods = {
  async getAllSchedules(): Promise<ScheduleI[]> {
    const { schedules } = await Schedule.getAllSchedules();
    return schedules;
  },
  async getSchedule(id: string): Promise<ScheduleI | null> {
    const { schedule } = await Schedule.getSchedule({ id });
    return schedule;
  },
  async saveSchedule(data: ScheduleI): Promise<void> {
    await Schedule.saveSchedule({ data });
  },
  async deleteSchedule(id: string): Promise<void> {
    await Schedule.deleteSchedule({ id });
  },
};

export default plugin;
