import goal from "./goal";
import preset from "./preset";

export default {
  goal,
  preset,
  selectors: {
    set apps(value: string) {
      sessionStorage.setItem("selectors_apps", value);
    },
    get apps() {
      return sessionStorage.getItem("selectors_apps");
    },
    set backUrl(value: string) {
      sessionStorage.setItem("selectors_backUrl", value);
    },
    get backUrl() {
      return sessionStorage.getItem("selectors_backUrl");
    },
  },
  action: {
    set type(value: string) {
      sessionStorage.setItem("action_type", value);
    },
    get type() {
      return sessionStorage.getItem("action_type");
    },
    set backUrl(value: string) {
      sessionStorage.setItem("action_back", value);
    },
    get backUrl() {
      return sessionStorage.getItem("action_back");
    },
  },
};
