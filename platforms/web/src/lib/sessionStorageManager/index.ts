import goal from "./goal";
export default {
  goal,
  selectors: {
    set apps(value: string) {
      sessionStorage.setItem("selectors_apps", value);
    },
    get apps() {
      return sessionStorage.getItem("selectors_apps");
    },
  },
  action: {
    set type(value: string) {
      sessionStorage.setItem("action_type", value);
    },
    get type() {
      return sessionStorage.getItem("action_type");
    },
    set back(value: string) {
      sessionStorage.setItem("action_back", value);
    },
    get back() {
      return sessionStorage.getItem("action_back");
    },
  },
};
