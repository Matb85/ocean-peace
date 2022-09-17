<script context="module" lang="ts">
  import { setupObserver } from "@redinnlabs/system/utils/Photo.svelte";

  setupObserver();
</script>

<script lang="ts">
  import "../app.css";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";
  import { beforeNavigate, goto } from "$app/navigation";
  import { page } from "$app/stores";
  import Navbar from "$lib/Navbar.svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  let tempTo: string;

  beforeNavigate(({ from, cancel, to }) => {
    if (from && to) {
      if (from.pathname == to.pathname) return;
      if (tempTo != to.pathname + to.search) {
        cancel();
        Api.fadeIn().then(() => {
          goto(to.pathname + to.search).then(() => {
            Api.fadeOut();
          });
        });
      }
      tempTo = to.pathname + to.search;
    }
  });
</script>

<main id="main">
  <slot />
</main>

{#if ["/", "/focus", "/profile"].includes($page.url.pathname)}
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
