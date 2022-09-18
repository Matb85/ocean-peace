<!-- displays the summary -->
<script lang="ts">
  import Confirmation from "$lib/Confirmation.svelte";
  import { Button } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import SelectedApps from "$lib/SelectedApps.svelte";
  import SelectedWebsites from "$lib/SelectedWebsites.svelte";

  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import type { AppIconI, GoalI } from "$schema";
  import { goTo } from "$lib/utils";
  import SM from "$lib/sessionManager";

  const goalSM = SM.goal.getProps("id", "name", "timeHours", "timeMinutes", "activeDays", "limitActionType");
  const dialogsSM = SM.dialogs.getProps("apps", "websites");

  let selectedApps: AppIconI[] = [];

  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(dialogsSM.apps));
  });
  /** save goal to a file
   * @returns {void}
   */
  function saveGoal() {
    isComplete = true;

    const data: GoalI = {
      id: goalSM.id,
      name: goalSM.name,
      apps: dialogsSM.apps,
      websites: dialogsSM.websites,
      limit: parseInt(goalSM.timeHours) * 60 + parseInt(goalSM.timeMinutes) + "",
      activeDays: goalSM.activeDays,
      limitActionType: goalSM.limitActionType,
    };
    setTimeout(() => {
      Api.saveGoal(data);
      goTo("/");
    }, 1500);
  }
  let isComplete = false;
</script>

<FullHeading backHref="/goal/edit/2">Summary</FullHeading>

<H tag={6} thin>Goal name</H>
<H tag={4} className="-mt-2" thin>{goalSM.name}</H>

<H tag={6} thin>Active Days</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse(goalSM.activeDays) as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>Limit</H>
<div class="flex flex-wrap justify-center gap-2 items-center">
  <H tag={4} className="mt-0 mb-0" thin>
    {goalSM.timeHours}h
    {parseInt(goalSM.timeMinutes)}min
  </H>
  <!---->
  <Button size="small">Time Period</Button>
</div>

<H tag={6} thin>Limit type</H>
<Button size="small">{goalSM.limitActionType}</Button>

<H tag={6} thin>Selected apps</H>
<SelectedApps apps={selectedApps} />

<H tag={6} thin>Allowed Websites</H>
<SelectedWebsites websites={JSON.parse(dialogsSM.websites)} />

<div on:click={saveGoal} class="fixed-bottom-button">
  <Button isFullWidth>save</Button>
</div>

<Confirmation {isComplete} text="Goal saved!" />
