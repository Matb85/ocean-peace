export default {
  set id(value: string) {
    sessionStorage.setItem("goal_id", value);
  },
  get id() {
    return sessionStorage.getItem("goal_id");
  },
  set name(value: string) {
    sessionStorage.setItem("goal_name", value);
  },
  get name() {
    return sessionStorage.getItem("goal_name");
  },
  set timeMinutes(value: string) {
    sessionStorage.setItem("goal_timeMinutes", value);
  },
  get timeMinutes() {
    return sessionStorage.getItem("goal_timeMinutes");
  },
  set timeHours(value: string) {
    sessionStorage.setItem("goal_timeHours", value);
  },
  get timeHours() {
    return sessionStorage.getItem("goal_timeHours");
  },
  set activeDays(value: string) {
    sessionStorage.setItem("goal_activeDays", value);
  },
  get activeDays() {
    return sessionStorage.getItem("goal_activeDays");
  },
  set limitActionType(value: string) {
    sessionStorage.setItem("goal_limitActionType", value);
  },
  get limitActionType() {
    return sessionStorage.getItem("goal_limitActionType");
  },
};
