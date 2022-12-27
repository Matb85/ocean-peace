<script lang="ts">
  import { Button, Aquarium, H } from "@redinnlabs/system/Elements";
  import { Goal } from "@redinnlabs/system/Units";
  import Cutout from "$lib/Cutout.svelte";

  import SM from "$lib/sessionManager";
  import Link from "$lib/Link.svelte";
  const curScreenTime: number = 100;
  const maxScreenTime: number = 270;
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { GoalI } from "$schema";
  import { onMount } from "svelte";
  import { t } from "$lib/i18n";
  import Navbar from "$lib/Navbar.svelte";
  import { numberToTime } from "$lib/utils";
  let allGoals: GoalI[] = [];
  onMount(async () => {
    SM.action.setProp("nativeBackUrl", "/");
    allGoals = await Api.getAllGoals();
    Api.getAllPermissions();
  });

  /** sets up data for a new goal
   * @returns nothing
   */
  function beforeAddGoal() {
    SM.goal.setProps({
      id: "goal" + Date.now(),
      name: "",
      limit: 1 * 60 + 15 + "",
      activeDays: "[]",
      limitActionType: "Notification",
      sessionTime: "0",
      sessionHistory: "[]",
    });
    SM.action.setProps({ type: "add", backUrl: "/", continueUrl: "/goal/edit/1" });
    SM.dialogs.setProps({ apps: "[]", websites: "[]" });
  }
  /** sets up data for an existing goal
   * @param i the index of the goal in the array of all the goals
   * @returns nothing
   */
  function beforeOpenGoal(i: number) {
    SM.goal.setProps({
      id: allGoals[i].id,
      name: allGoals[i].name,
      limit: allGoals[i].limit,
      activeDays: allGoals[i].activeDays,
      limitActionType: allGoals[i].limitActionType,
      sessionTime: allGoals[i].sessionTime,
      sessionHistory: allGoals[i].sessionHistory,
    });
    SM.dialogs.setProps({ apps: allGoals[i].apps, websites: allGoals[i].websites });
  }
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
  <div class="text-shadow text-white absolute w-full bottom-24 grid grid-cols-1 place-items-center">
    <H noMargins>{$t("d.screentime.your")}</H>
    <H tag={2} noMargins className="text-shadow-sm">
      {@const time = numberToTime(curScreenTime)}
      {time[0] + "h " + time[1] + "min"}
    </H>
    <H tag={4} noMargins>
      {@const time = numberToTime(maxScreenTime - curScreenTime)}
      {time[0] + "h " + time[1] + "min"}
      {$t("d.left")}
    </H>
  </div>
</Link>

<!-- goals display -->
<H thin>{$t("d.goal.your")}</H>
<div class="card-flex-col">
  {#each allGoals as goal, i}
    <Link href="/goal?id={goal.id}" on:click={() => beforeOpenGoal(i)} className="w-full">
      <Goal
        percentage={(parseInt(goal.limit) - parseInt(goal.sessionTime, 0) / (1000 * 60)) / parseInt(goal.limit) < 0
          ? 0
          : ((parseInt(goal.limit) - parseInt(goal.sessionTime, 0) / (1000 * 60)) / parseInt(goal.limit)) * 100}
        title={goal.name}
        info={JSON.parse(goal.activeDays).join(", ")}
      />
    </Link>
  {/each}
  {#if allGoals.length == 0}
    <p>{$t("d.goal.no")}</p>
  {/if}
  <Link href="/goal/edit/1" on:click={beforeAddGoal}>
    <Button>{$t("d.goal.add")}</Button>
  </Link>
</div>

<Navbar />
