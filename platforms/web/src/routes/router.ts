import index from "./index.svelte";
import insights from "./insights.svelte";
import goalRouter from "./goal/routes";
import dialogRouter from "./dialogs/router";
import profileRouter from "./profile/router";
import focusRouter from "./focus/router";
import setupRouter from "./setup/router";
const routes = {
  // Exact path
  "/": index,
  "/insights": insights,
  ...goalRouter,
  ...dialogRouter,
  ...profileRouter,
  ...focusRouter,
  ...setupRouter,
};

export default routes;
