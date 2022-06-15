<script context="module" lang="ts">
  import { setupObserver } from "@redinnlabs/system/utils/Photo.svelte";

  setupObserver();
</script>

<script lang="ts">
  import "../app.css";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";
  import { beforeNavigate, goto } from "$app/navigation";
  import { page } from "$app/stores";
  import { onMount } from "svelte";
  import Navbar from "$lib/Navbar.svelte";
  let body;
  let tempTo: string;
  onMount(() => {
    body = document.getElementById("main");
  });
  beforeNavigate(({ from, cancel, to }) => {
    if (from) {
      new Promise<void>(resolve => {
        body.classList.add("during-transition");
        if (tempTo != to.pathname) {
          cancel();
          setTimeout(() => {
            goto(to.pathname);
            setTimeout(() => {
              body.classList.remove("during-transition");
              resolve();
            }, 50);
          }, 300);
        }
        tempTo = to.pathname;
      });
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

<style global>
  @import "@redinnlabs/system/utils/base.css";

  :global(#main) {
    transition-duration: 300ms;
    transition-property: transform, opacity;
    transform: translateY(0rem);
    opacity: 1;
  }

  :global(#main.during-transition) {
    transform: translateY(-5rem);
    opacity: 0;
  }
</style>
