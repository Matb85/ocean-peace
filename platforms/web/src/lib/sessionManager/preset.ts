export default {
  set id(value: string) {
    sessionStorage.setItem("preset_id", value);
  },
  get id() {
    return sessionStorage.getItem("preset_id");
  },
  set name(value: string) {
    sessionStorage.setItem("preset_name", value);
  },
  get name() {
    return sessionStorage.getItem("preset_name");
  },
  set icon(value: string) {
    sessionStorage.setItem("preset_icon", value);
  },
  get icon() {
    return sessionStorage.getItem("preset_icon");
  },
};
