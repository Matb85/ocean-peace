<script>
  import { Router } from "@roxi/routify";
  import routes from "../.routify/routes.default.js";

</script>

<Router {routes} />

<style
  global>
  @tailwind base;
  @tailwind utilities;
  svg {display:block;}
</style>