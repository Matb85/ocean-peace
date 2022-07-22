import { registerPlugin } from "@capacitor/core";
import type { GoalI, GoalsMethods } from "@redinn/oceanpeace-web/api/goals";

interface GoalsPlugin {
  getAllGoals(): Promise<{ goals: GoalI[] }>;
  getGoal(data: { id: string }): Promise<{ goal: GoalI }>;
  saveGoal(data: GoalI): void;
  deleteGoal(data: { id: string }): void;
}

const Goal = registerPlugin<GoalsPlugin>("Goal");

const plugin: GoalsMethods = {
  async getAllGoals(): Promise<GoalI[]> {
    const { goals } = await Goal.getAllGoals();
    return goals;
  },
  async getGoal(id: string): Promise<GoalI | null> {
    const { goal } = await Goal.getGoal({ id });
    return goal;
  },
  async saveGoal(data: GoalI): Promise<void> {
    await Goal.saveGoal(data);
  },
  async deleteGoal(id: string): Promise<void> {
    await Goal.deleteGoal({ id });
  },
};

export default plugin;
