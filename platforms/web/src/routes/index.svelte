<script lang="ts">
  import { Button, Aquarium } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import Cutout from "$lib/Cutout.svelte";
  import H from "$lib/H.svelte";
  import { beforeNavigate } from "$app/navigation";

  export let curScreenTime: number = 100;
  export let maxScreenTime: number = 270;

  beforeNavigate(({ to }) => {
    if (to.pathname == "/goal/edit/1") {
      sessionStorage.setItem("edit_goal_apps", "[]");
      sessionStorage.setItem("edit_goal_name", "");
      sessionStorage.setItem("edit_goal_time_minutes", "3");
      sessionStorage.setItem("edit_goal_time_hours", "1");
      sessionStorage.setItem("edit_goal_active_days", "[]");
      sessionStorage.setItem("edit_goal_limit_type", "Notification");
      sessionStorage.setItem("edit_goal_action_type", "Add");
      sessionStorage.setItem("edit_goal_action_back", "/");
    }
  });
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
  {#each Array(4) as _}
    <a sveltekit:prefetch href="/goal" class="w-full">
      <Goal title={"Some goal here"} info={"something left"} />
    </a>
  {/each}
  <a sveltekit:prefetch href="/goal/edit/1">
    <Button>Add Goal</Button>
  </a>
</div>
