<script lang="ts">
  import { Background } from "@redinnlabs/system/Elements";
  import LanguageSelector from "$lib/LanguageSelector.svelte";
</script>

<div class="background">
  <Background className="background-svg" x={50} y={100} />
</div>

<hgroup class="w-11/12 relative mt-12">
  <slot name="header" />
</hgroup>

<section class="card-flex-col shadow-wrapper w-9/12 h-[60vh] max-w-sm absolute bottom-16 bg-white p-4">
  <slot />
</section>
<LanguageSelector className="absolute bottom-4" />

<style lang="postcss">
  .background {
    @apply w-full h-full absolute top-0 -z-10 overflow-hidden;
  }
  .background :global(.background-svg) {
    @apply absolute -bottom-[10%];
    transform: translate(15%, 15rem) scale(2.5) rotate(-72deg);
  }
</style>
