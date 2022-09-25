<script lang="ts">
  import "../app.css";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";
  import Router, { replace, location } from "svelte-spa-router";
  import routes from "./router";
  import Navbar from "$lib/Navbar.svelte";
  import { setupObserver } from "@redinnlabs/system/utils/Photo.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  setupObserver();

  import { locale, loadTranslations } from "$lib/i18n";

  const defaultLocale = "en"; // get from cookie, user session, ...
  const initLocale = locale.get() || localStorage.getItem("oceanpeace_lang") || defaultLocale; // set default if no locale already set
  loadTranslations(initLocale, $location); // keep this just before the `return`

  Api.getPreferences().then(async data => {
    if (data.setupComplete || $location.startsWith("/setup/")) return;
    await Api.fadeIn();
    replace("/setup/1");
    setTimeout(() => Api.fadeOut(), 75);
  });
</script>

<main id="main">
  <Router {routes} />
</main>

{#if ["/", "/focus", "/profile"].includes($location)}
  <Navbar />
{/if}

<GlobalGradient />

<style global lang="postcss">
  @import "@redinnlabs/system/utils/base.css";
  #main {
    @apply flex flex-col items-center gap-4 pb-32;
    scrollbar-width: none;
  }
</style>
