<script>
  import { Router } from "@roxi/routify";
  import routes from "../.routify/routes.default.js";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";

</script>

<Router {routes} />
<GlobalGradient />

<style
  global>
  @tailwind base;
  @tailwind utilities;
  svg {display:block;}
</style>