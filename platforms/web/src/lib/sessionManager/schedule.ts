export default {
  set id(value: string) {
    sessionStorage.setItem("schedule_id", value);
  },
  get id() {
    return sessionStorage.getItem("schedule_id");
  },
  set name(value: string) {
    sessionStorage.setItem("schedule_name", value);
  },
  get name() {
    return sessionStorage.getItem("schedule_name");
  },
  set preset(value: string) {
    sessionStorage.setItem("schedule_preset", value);
  },
  get preset() {
    return sessionStorage.getItem("schedule_preset");
  },
  set activeDays(value: string) {
    sessionStorage.setItem("schedule_activeDays", value);
  },
  get activeDays() {
    return sessionStorage.getItem("schedule_activeDays");
  },
  set startTime(value: string) {
    sessionStorage.setItem("schedule_startTime", value);
  },
  get startTime() {
    return sessionStorage.getItem("schedule_startTime");
  },
  set stopTime(value: string) {
    sessionStorage.setItem("schedule_stopTime", value);
  },
  get stopTime() {
    return sessionStorage.getItem("schedule_stopTime");
  },
};
