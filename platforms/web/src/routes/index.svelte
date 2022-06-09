<script lang="ts">
  import { Button, Aquarium } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import Navbar from "$lib/Navbar.svelte";
  import Cutout from "$lib/Cutout.svelte";
  import H from "$lib/H.svelte";

  export let curScreenTime: number = 100;
  export let maxScreenTime: number = 270;
</script>

<!-- aquarium background -->
<a sveltekit:prefetch href="/insights" class="w-full h-80 block relative">
  <Aquarium
    percent={(maxScreenTime - curScreenTime) / maxScreenTime < 0
      ? 0
      : ((maxScreenTime - curScreenTime) / maxScreenTime) * 100}
  />
  <!-- cut out-->
  <Cutout className="w-full bottom-0 absolute" />

  <!-- screen time display -->
  <div class="text-shadow text-white absolute w-full bottom-20 grid grid-cols-1 place-items-center">
    <H noMargins>Your Screen time</H>
    <H tag={2} noMargins className="text-shadow-sm">
      {curScreenTime < 59 ? "" : Math.floor(curScreenTime / 60) + "h"}
      {curScreenTime % 60 == 0 ? "" : (curScreenTime % 60) + "min"}
    </H>
    <H tag={4} noMargins>
      {Math.floor((maxScreenTime - curScreenTime) / 60) + "h"}
      {(maxScreenTime - curScreenTime) % 60 == 0 ? "" : ((maxScreenTime - curScreenTime) % 60) + "min"}
      left
    </H>
  </div>
</a>

<!-- focus button -->
<a sveltekit:prefetch href="/focus">
  <Button>Start a focus session</Button>
</a>

<!-- goals display -->
<H thin>Your Goals</H>
<div class="card-flex-col">
  {#each Array(4) as _, i}
    <a sveltekit:prefetch href="/goal" class="w-full">
      <Goal title={"Some goal here"} info={"something left"} className="Goal" />
    </a>
  {/each}
  <a sveltekit:prefetch href="/goale/dit/1">
    <Button secondary="true">Add Goal</Button>
  </a>
</div>

<Navbar />
