<script context="module" lang="ts">
  import { setupObserver } from "@redinnlabs/system/utils/Photo.svelte";

  setupObserver();
</script>

<script lang="ts">
  import "../app.css";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";
  import { beforeNavigate, goto } from "$app/navigation";
  import { onMount } from "svelte";

  let body;
  let tempTo: string;
  onMount(() => {
    body = document.getElementById("svelte");
  });
  beforeNavigate(({ from, cancel, to }) => {
    if (from) {
      new Promise<void>(resolve => {
        body.classList.add("during-transition");
        if (tempTo != to.pathname) {
          cancel();
          setTimeout(() => {
            console.log(to.pathname);
            goto(to.pathname);
            body.classList.remove("during-transition");
            resolve();
          }, 300);
        }
        tempTo = to.pathname;
      });
    }
  });
</script>

<slot />

<GlobalGradient />

<style global>
  @import "@redinnlabs/system/utils/base.css";

  :global(body) {
    transition-duration: 300ms;
    transition-property: transform, opacity;
    transform: translateY(0rem);
    opacity: 1;
  }

  :global(body.during-transition) {
    transform: translateY(-5rem);
    opacity: 0;
  }
</style>
