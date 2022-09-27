import { registerPlugin } from "@capacitor/core";
import type { GoalI, GoalsMethods } from "@redinn/oceanpeace-web/api/goals";

interface GoalsPlugin {
  getAllGoals(): Promise<{ goals: GoalI[] }>;
  getGoal(data: { id: string }): Promise<{ goal: GoalI }>;
  saveGoal(data: { data: GoalI }): Promise<void>;
  deleteGoal(data: { id: string }): Promise<void>;
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
  saveGoal(data: GoalI): Promise<void> {
    return Goal.saveGoal({ data });
  },
  deleteGoal(id: string): Promise<void> {
    return Goal.deleteGoal({ id });
  },
};

export default plugin;
