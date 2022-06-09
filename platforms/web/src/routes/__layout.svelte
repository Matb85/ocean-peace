<script context="module" lang="ts">
  import { setupObserver } from "@redinnlabs/system/utils/Photo.svelte";
  setupObserver();
</script>

<script lang="ts">
  import "../app.css";
  import GlobalGradient from "@redinnlabs/system/utils/GlobalGradient.svelte";
</script>

<slot />

<GlobalGradient />

<style global>
  @import "@redinnlabs/system/utils/base.css";
</style>
