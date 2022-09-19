import { wrap } from "svelte-spa-router/wrap";
const routes = {
  // Exact path
  "/setup/1": wrap({ asyncComponent: () => import("./1.svelte") }),
  "/setup/2": wrap({ asyncComponent: () => import("./2.svelte") }),
};

export default routes;
