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
  let className = "";
  let tempTo: string;

  beforeNavigate(({ from, cancel, to }) => {
    if (from) {
      new Promise<void>(resolve => {
        className = "during-transition";
        if (tempTo != to.pathname) {
          cancel();
          setTimeout(() => {
            goto(to.pathname);
            setTimeout(() => {
              className = "";
              resolve();
            }, 200);
          }, 200);
        }
        tempTo = to.pathname;
      });
    }
  });
</script>

<main id="main" class={className}>
  <slot />
</main>

{#if ["/", "/focus", "/profile"].includes($page.url.pathname)}
  <Navbar />
{/if}

<GlobalGradient />

<style global>
  @import "@redinnlabs/system/utils/base.css";

  #main {
    @apply flex flex-col items-center gap-4 pb-16;
    scrollbar-width: none;
    transition-duration: 200ms;
    transition-property: transform, opacity;
    transform: translateY(0rem);
    opacity: 1;
  }
  #main.during-transition {
    transform: translateY(-5rem);
    opacity: 0;
  }
</style>
