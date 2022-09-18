<script lang="ts">
  import "../app.css";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";
  import Router, { location } from "svelte-spa-router";
  import routes from "./router";
  import Navbar from "$lib/Navbar.svelte";
  import { setupObserver } from "@redinnlabs/system/utils/Photo.svelte";

  setupObserver();
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
