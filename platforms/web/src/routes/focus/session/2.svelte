<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button, Background } from "@redinnlabs/system/Elements";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import H from "$lib/H.svelte";

  const minutes: number = 148;
</script>

<H tag={3} className="mt-5 text-white">Summary</H>

<div class="w-10/12 text-center text-white mt-16">
  <H tag={1}>
    {minutes < 59 ? "" : Math.floor(minutes / 60) + "h"}
    {minutes % 60 == 0 ? "" : (minutes % 60) + "min"}
  </H>
  <H tag={6} thin>of focus</H>
</div>

<div class="flex gap-1 items-end text-white">
  <H tag={2}>+45</H>
  <H tag={4}>points</H>
</div>

<H tag={5} className="mt-36">Notifications received</H>

<SelectedApps />

<a sveltekit:prefetch href="/" class="fixed bottom-10">
  <Button>continue</Button>
</a>

<div class="background">
  <Background className="background-svg" x={50} y={100} />
</div>

<style lang="postcss">
  .background {
    @apply w-full h-full absolute -z-10 overflow-hidden;
  }
  .background :global(.background-svg) {
    @apply absolute -top-[60%];
    transform: translate(15%, 5%) scale(3) rotate(72deg);
  }
</style>
