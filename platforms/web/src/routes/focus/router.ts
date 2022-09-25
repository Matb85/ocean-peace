import { wrap } from "svelte-spa-router/wrap";
import index from "./index.svelte";

const routes = {
  // Exact path
  "/focus": index,
  "/focus/schedule": wrap({ asyncComponent: () => import("./schedule.svelte") }),
  "/focus/preset": wrap({ asyncComponent: () => import("./preset.svelte") }),
  "/focus/session/1": wrap({ asyncComponent: () => import("./session/1.svelte") }),
  "/focus/session/2": wrap({ asyncComponent: () => import("./session/2.svelte") }),
  "/focus/editschedule/1": wrap({ asyncComponent: () => import("./editschedule/1.svelte") }),
  "/focus/editschedule/2": wrap({ asyncComponent: () => import("./editschedule/2.svelte") }),
  "/focus/editschedule/3": wrap({ asyncComponent: () => import("./editschedule/3.svelte") }),
  "/focus/editschedule/delete": wrap({ asyncComponent: () => import("./editschedule/delete.svelte") }),
  "/focus/editpreset/1": wrap({ asyncComponent: () => import("./editpreset/1.svelte") }),
  "/focus/editpreset/2": wrap({ asyncComponent: () => import("./editpreset/2.svelte") }),
  "/focus/editpreset/3": wrap({ asyncComponent: () => import("./editpreset/3.svelte") }),
  "/focus/editpreset/delete": wrap({ asyncComponent: () => import("./editpreset/delete.svelte") }),
};

export default routes;
