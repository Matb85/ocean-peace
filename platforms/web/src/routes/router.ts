import index from "./index.svelte";
import insights from "./insights.svelte";
import goalRouter from "./goal/routes";
import dialogRouter from "./dialogs/router";
import profileRouter from "./profile/router";
import focusRouter from "./focus/router";

const routes = {
  // Exact path
  "/": index,
  "/insights": insights,
  ...goalRouter,
  ...dialogRouter,
  ...profileRouter,
  ...focusRouter,
};

export default routes;
