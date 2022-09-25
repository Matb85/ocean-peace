import index from "./index.svelte";
import { wrap } from "svelte-spa-router/wrap";

const routes = {
  // Exact path
  "/goal": index,
  "/goal/delete": wrap({ asyncComponent: () => import("./delete.svelte") }),
  "/goal/edit/1": wrap({ asyncComponent: () => import("./edit/1.svelte") }),
  "/goal/edit/2": wrap({ asyncComponent: () => import("./edit/2.svelte") }),
  "/goal/edit/3": wrap({ asyncComponent: () => import("./edit/3.svelte") }),
};

export default routes;
