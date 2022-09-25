import index from "./index.svelte";
import { wrap } from "svelte-spa-router/wrap";
const routes = {
  // Exact path
  "/profile": index,
  "/profile/settings": wrap({ asyncComponent: () => import("./settings.svelte") }),
};

export default routes;
