import goal from "./goal";
import preset from "./preset";

export default {
  goal,
  preset,
  dialogs: {
    set apps(value: string) {
      sessionStorage.setItem("dialogs_apps", value);
    },
    get apps() {
      return sessionStorage.getItem("dialogs_apps");
    },
    set backUrl(value: string) {
      sessionStorage.setItem("dialogs_backUrl", value);
    },
    get backUrl() {
      return sessionStorage.getItem("dialogs_backUrl");
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
    set continueUrl(value: string) {
      sessionStorage.setItem("dialogs_continueUrl", value);
    },
    get continueUrl() {
      return sessionStorage.getItem("dialogs_continueUrl");
    },
  },
};
