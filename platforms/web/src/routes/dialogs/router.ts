import { wrap } from "svelte-spa-router/wrap";
const routes = {
  // Exact path
  "/dialogs/cancel": wrap({ asyncComponent: () => import("./cancel.svelte") }),
  "/dialogs/websites": wrap({ asyncComponent: () => import("./websites.svelte") }),
  "/dialogs/apps": wrap({ asyncComponent: () => import("./apps.svelte") }),
};

export default routes;
