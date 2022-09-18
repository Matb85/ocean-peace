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

  let selectedApps: AppIconI[] = [];

  onMount(async () => {
    selectedApps = await Api.getAppIcons(JSON.parse(SM.dialogs.apps));
  });
  /** save goal to a file
   * @returns {void}
   */
  function saveGoal() {
    isComplete = true;
    const data: GoalI = {
      id: SM.goal.id,
      name: SM.goal.name,
      apps: SM.dialogs.apps,
      websites: SM.dialogs.websites,
      limit: parseInt(SM.goal.timeHours) * 60 + parseInt(SM.goal.timeMinutes) + "",
      activeDays: SM.goal.activeDays,
      limitActionType: SM.goal.limitActionType,
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
<H tag={4} className="-mt-2" thin>{SM.goal.name}</H>

<H tag={6} thin>Active Days</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse(SM.goal.activeDays) as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>Limit</H>
<div class="flex flex-wrap justify-center gap-2 items-center">
  <H tag={4} className="mt-0 mb-0" thin>
    {SM.goal.timeHours}h
    {parseInt(SM.goal.timeMinutes)}min
  </H>
  <!---->
  <Button size="small">Time Period</Button>
</div>

<H tag={6} thin>Limit type</H>
<Button size="small">{SM.goal.limitActionType}</Button>

<H tag={6} thin>Selected apps</H>
<SelectedApps apps={selectedApps} />

<H tag={6} thin>Allowed Websites</H>
<SelectedWebsites websites={JSON.parse(SM.dialogs.websites)} />

<div on:click={saveGoal} class="fixed-bottom-button">
  <Button isFullWidth>save</Button>
</div>

<Confirmation {isComplete} />
