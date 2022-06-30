export interface GoalI {
  id: string;
  name: string;
  apps: string;
  limit: string;
  activeDays: string;
  limitActionType: string;
}

export interface GoalsMethods {
  // G O A L S   A P I

  /** Returns JSON with each goal JSON as described below
   * @returns all goals
   */
  getAllGoals(): Promise<GoalI[]>;
  /** Returns goal JSON as described below
   * @param id the id of the goal
   */
  getGoal(id: string): Promise<GoalI | null>;
  /** Creates a goal and saves it to a file.
   * @param {GoalI} data data
   */
  saveGoal(data: GoalI): Promise<void>;
  /** Deletes selected goal and its file
   * @param id the id of the goal
   */
  deleteGoal(id: string): Promise<void>;
}
