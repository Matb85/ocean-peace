import { registerPlugin } from "@capacitor/core";
import type { ScheduleI, ScheduleMethods } from "@redinn/oceanpeace-web/api/schedule";

interface SchedulesPlugin {
  getAllSchedules(): Promise<{ schedules: ScheduleI[] }>;
  getSchedule(data: { id: string }): Promise<{ schedule: ScheduleI }>;
  saveSchedule(data: { data: ScheduleI }): Promise<void>;
  deleteSchedule(data: { id: string }): Promise<void>;
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
  saveSchedule(data: ScheduleI): Promise<void> {
    return Schedule.saveSchedule({ data });
  },
  deleteSchedule(id: string): Promise<void> {
    return Schedule.deleteSchedule({ id });
  },
};

export default plugin;
