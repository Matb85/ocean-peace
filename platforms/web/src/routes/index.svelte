<script lang="ts">
  import { Button, Heading, Aquarium } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import Navbar from "$lib/Navbar.svelte";
  import Cutout from "$lib/Cutout.svelte";

  export let curScreenTime: number = 100;
  export let maxScreenTime: number = 270;
</script>

<!-- aquarium background -->
<div class="w-full h-80 block relative">
  <Aquarium
    percent={(maxScreenTime - curScreenTime) / maxScreenTime < 0
      ? 0
      : ((maxScreenTime - curScreenTime) / maxScreenTime) * 100}
  />
  <!-- cut out-->
  <Cutout className="w-full bottom-0 absolute" />

  <!-- screen time display -->
  <div class="text-shadow  absolute w-full bottom-20 grid grid-cols-1 place-items-center">
    <Heading tag={5} className="text-white">Your Screen time</Heading>
    <Heading tag={2} className="text-white text-shadow-sm">
      {curScreenTime < 59 ? "" : Math.floor(curScreenTime / 60) + "h"}
      {curScreenTime % 60 == 0 ? "" : (curScreenTime % 60) + "min"}
    </Heading>
    <Heading tag={4} className="text-white">
      {Math.floor((maxScreenTime - curScreenTime) / 60) + "h"}
      {(maxScreenTime - curScreenTime) % 60 == 0 ? "" : ((maxScreenTime - curScreenTime) % 60) + "min"}
      left
    </Heading>
  </div>
</div>

<!-- focus button -->
<a sveltekit:prefetch href="/focus">
  <Button>Start a focus session</Button>
</a>

<!-- goals display -->
<Heading tag={6} className="!font-normal">Your Goals</Heading>
<div class="place-content-center place-items-center grid grid-cols-1 w-11/12 gap-5">
  {#each Array(4) as _, i}
    <a sveltekit:prefetch href="/goal" class="w-full">
      <Goal title={"Some goal here"} info={"something left"} className="Goal" />
    </a>
  {/each}
  <a sveltekit:prefetch href="/goaledit">
    <Button secondary="true">Add Goal</Button>
  </a>
</div>

<Navbar />
