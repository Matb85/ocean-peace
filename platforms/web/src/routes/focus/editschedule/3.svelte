<!-- displays the summary -->
<script lang="ts">
  import Confirmation from "$lib/Confirmation.svelte";
  import { Preset } from "@redinnlabs/system/Units";
  import { Button } from "@redinnlabs/system/Elements";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";

  import { onMount } from "svelte";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { goTo } from "$lib/utils";
  import type { PresetI, ScheduleI } from "$schema";
  import SM from "$lib/sessionManager";

  let preset: Partial<PresetI> = {};

  onMount(async () => {
    preset = await Api.getPreset(SM.schedule.preset);
  });
  /** save schedule to a file
   * @returns {void}
   */
  function saveSchedule() {
    isComplete = true;
    setTimeout(async () => {
      const data: ScheduleI = {
        id: SM.schedule.id,
        name: SM.schedule.name,
        preset: SM.schedule.preset,
        activeDays: SM.schedule.activeDays,
        startTime: SM.schedule.startTime,
        stopTime: SM.schedule.stopTime,
      };
      await Api.saveSchedule(data);
      goTo("/focus");
    }, 1500);
  }
  let isComplete = false;
</script>

<FullHeading backHref="/focus/editschedule/2">Summary</FullHeading>

<H tag={6} thin>Rule name</H>
<H tag={4} className="-mt-2" thin>{SM.schedule.name}</H>

<H tag={6} thin>Preset for this schedule</H>
<Preset src={preset.icon} label={preset.name} />

<H tag={6} thin>Days active</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse(SM.schedule.activeDays) as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>Hours active</H>
<H tag={4} className="-mt-2" thin>
  {Math.floor(parseInt(SM.schedule.startTime) / 60)}:{Math.floor(parseInt(SM.schedule.startTime) % 60)}
  -
  {Math.floor(parseInt(SM.schedule.stopTime) / 60)}:{Math.floor(parseInt(SM.schedule.stopTime) % 60)}
</H>

<div on:click={saveSchedule} class="fixed-bottom-button">
  <Button isFullWidth>save</Button>
</div>

<Confirmation {isComplete} />
