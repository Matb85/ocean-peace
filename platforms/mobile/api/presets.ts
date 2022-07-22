import { registerPlugin } from "@capacitor/core";
import type { PresetI, PresetsMethods } from "@redinn/oceanpeace-web/api/presets";

interface GoalsPlugin {
  getAllGoals(): Promise<{ goals: PresetI[] }>;
  getGoal(data: { id: string }): Promise<{ goal: PresetI }>;
  saveGoal(data: PresetI): void;
  deleteGoal(data: { id: string }): void;
}

const Goal = registerPlugin<GoalsPlugin>("Goal");

const plugin: PresetsMethods = {
  async getAllPresets(): Promise<PresetI[]> {
    const { goals } = await Goal.getAllGoals();
    return goals;
  },
  async getPreset(id: string): Promise<PresetI | null> {
    const { goal } = await Goal.getGoal({ id });
    return goal;
  },
  async savePreset(data: PresetI): Promise<void> {
    await Goal.saveGoal(data);
  },
  async deletePreset(id: string): Promise<void> {
    await Goal.deleteGoal({ id });
  },
};

export default plugin;
