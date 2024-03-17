<script lang="ts">
  import "../app.css";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";
  import Router, { replace, location } from "svelte-spa-router";
  import routes from "./router";
  import { setupObserver } from "@redinnlabs/system/utils/Photo.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import SM from "$lib/sessionManager";
  import { goTo } from "$lib/utils";

  setupObserver();

  import { locale, loadTranslations } from "$lib/i18n";
  const defaultLocale = "pl"; // get from cookie, user session, ...
  const initLocale = locale.get() || localStorage.getItem("oceanpeace_lang") || defaultLocale; // set default if no locale already set
  loadTranslations(initLocale, $location); // keep this just before the `return`

  Api.getPreferences().then(async data => {
    setTimeout(() => Api.hideSplash(), 100);
    if (data.setupComplete || $location.startsWith("/setup/")) return;
    replace("/setup/1");
  });

  Api.setUpNativeBackButton(() => {
    goTo(SM.action.getProp("nativeBackUrl") || "/");
  });
</script>

<main id="main">
  <Router {routes} />
</main>
<GlobalGradient />

<style global lang="postcss">
  @import "@redinnlabs/system/utils/base.css";
  #main {
    @apply flex flex-col items-center gap-4 pb-32 min-h-screen;
    scrollbar-width: none;
  }
</style>
