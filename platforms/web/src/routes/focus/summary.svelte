<script lang="ts">
  import "@redinnlabs/system/utils/base.css";
  import { Button, Heading, Background } from "@redinnlabs/system/Elements";
  import SelectedApps from "$lib/SelectedApps.svelte";

  const minutes: number = 148;
</script>

<Heading tag={3} className="  mt-5 text-white">Summary</Heading>

<div class="w-10/12 text-center text-white mt-16">
  <Heading tag={1}>
    {minutes < 59 ? "" : Math.floor(minutes / 60) + "h"}
    {minutes % 60 == 0 ? "" : (minutes % 60) + "min"}
  </Heading>
  <Heading tag={6} className="!font-normal">of focus</Heading>
</div>

<div class="flex gap-1 items-end mt-8">
  <Heading tag={2} className="text-white">+45</Heading>
  <Heading tag={4} className="text-white">points</Heading>
</div>

<Heading tag={5} className="text-center mt-24">Notifications received</Heading>

<SelectedApps />

<a sveltekit:prefetch href="/" class="fixed bottom-10">
  <Button>continue</Button>
</a>

<div class="background">
  <Background className="background-svg" x={50} y={100} />
</div>

<style>
  .background {
    @apply w-full h-full absolute -z-10 overflow-hidden;
  }
  .background :global(.background-svg) {
    @apply absolute -top-1/2;
    transform: translate(15%, 5%) scale(2.5) rotate(180deg);
  }
</style>
