<script lang="ts">
  import { Button, Heading, Aquarium } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import Navbar from "../lib/Navbar.svelte";
  import shape from "../assets/white-pointy-cutout.svg";

  export let curScreenTime: number = 100;
  export let maxScreenTime: number = 270;
</script>

<main>
  <!-- content -->
  <div class="content">
    <!-- aquarium background -->
    <div class="w-full h-80 block relative">
      <Aquarium
        percent={(maxScreenTime - curScreenTime) / maxScreenTime < 0
          ? 0
          : ((maxScreenTime - curScreenTime) / maxScreenTime) * 100}
      />
      <!-- cut out-->
      <img src={shape} alt="shape" class="w-full bottom-0 absolute" />

      <!-- screen time display -->
      <div class="screentime text-shadow">
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
    <div class="startFocus_btn">
      <a href="/focus">
        <Button>Start a focus session</Button>
      </a>
    </div>

    <!-- goals display -->
    <div class="goals_title">
      <Heading tag={6} className="!font-normal">Your Goals</Heading>
    </div>
    <div class="goals">
      {#each Array(4) as _, i}
        <a href="/goal" class="w-full">
          <Goal title={"Some goal here"} info={"something left"} className="Goal" />
        </a>
      {/each}
      <a href="/goaledit">
        <Button secondary="true">Add Goal</Button>
      </a>
    </div>

    <!-- content div-->
  </div>

  <Navbar />
</main>

<style lang="postcss">
  :root {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans",
      "Helvetica Neue", sans-serif;
  }

  .screentime {
    @apply absolute
      w-full
      bottom-20
      grid
      grid-cols-1
      place-items-center;
  }
  .content {
    pointer-events: all;
    scrollbar-width: none;
    @apply flex
        flex-col
        gap-5
        items-center
        place-content-center
        select-none
        mb-16;
  }

  .startFocus_btn {
  }

  .goals_title {
  }

  .goals {
    @apply place-content-center place-items-center
      grid grid-cols-1
      w-11/12
      gap-5;
  }

  .text-shadow {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.4));
  }
</style>
