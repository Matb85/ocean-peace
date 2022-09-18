<script lang="ts">
  import { Button, Aquarium } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import Cutout from "$lib/Cutout.svelte";
  import H from "$lib/H.svelte";
  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  export let curScreenTime: number = 100;
  export let maxScreenTime: number = 270;

  onDestroy(() => {
    SM.goal.id = "goal" + Date.now();
    SM.goal.name = "";
    SM.goal.timeMinutes = "15";
    SM.goal.timeHours = "1";
    SM.goal.activeDays = "[]";
    SM.goal.limitActionType = "Notification";
    SM.action.type = "Add";
    SM.action.backUrl = "/";
    SM.action.continueUrl = "/goal/edit/1";

    SM.dialogs.apps = "[]";
    SM.dialogs.websites = "[]";
  });

  import Api from "@redinn/oceanpeace-mobile/api";
  import type { GoalI } from "$schema";
  import { onMount, onDestroy } from "svelte";
  let allGoals: GoalI[] = [];
  onMount(async () => {
    allGoals = await Api.getAllGoals();
  });
</script>

<!-- aquarium background -->
<Link href="/insights" className="w-full h-80 block relative">
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
</Link>

<!-- focus button -->
<Link href="/focus">
  <Button>Start a focus session</Button>
</Link>

<!-- goals display -->
<H thin>Your Goals</H>
<div class="card-flex-col">
  {#each allGoals as goal}
    <Link href="/goal?id={goal.id}" className="w-full">
      <Goal percentage={Math.random() * 100} title={goal.name} info={JSON.parse(goal.activeDays).join(", ")} />
    </Link>
  {/each}
  {#if allGoals.length == 0}
    <p>No goals</p>
  {/if}
  <Link href="/goal/edit/1">
    <Button>Add Goal</Button>
  </Link>
</div>
