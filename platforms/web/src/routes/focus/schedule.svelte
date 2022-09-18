<script lang="ts">
  import { stringTimeFromNumber } from "$lib/utils";
  import FullHeading from "$lib/FullHeading.svelte";
  import DangerZone from "$lib/DangerZone.svelte";
  import H from "$lib/H.svelte";
  import { Preset } from "@redinnlabs/system/Units";
  import { Button } from "@redinnlabs/system/Elements";
  import type { ScheduleI, PresetI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";
  import { onMount } from "svelte";
  import SM from "$lib/sessionManager";
  import { querystring } from "svelte-spa-router";

  const scheduleId = new URLSearchParams($querystring).get("id");

  let scheduleData: Partial<ScheduleI> = {};
  let presetData: Partial<PresetI> = {};
  onMount(async () => {
    scheduleData = await Api.getSchedule(scheduleId);
    presetData = await Api.getPreset(scheduleData.preset);

    SM.schedule.id = scheduleData.id;
    SM.schedule.name = scheduleData.name;
    SM.schedule.preset = scheduleData.preset;
    SM.schedule.activeDays = scheduleData.activeDays;
    SM.schedule.startTime = scheduleData.startTime;
    SM.schedule.stopTime = scheduleData.stopTime;

    SM.action.type = "Edit";
    SM.action.backUrl = "/focus/schedule?" + $querystring;
    SM.action.continueUrl = "/focus/editschedule/1";
  });
</script>

<FullHeading backHref="/focus" editHref="/focus/editschedule/1">Schedule</FullHeading>

<H tag={6} thin>Rule name</H>
<H tag={4} className="-mt-2" thin>{scheduleData.name}</H>

<H tag={6} thin>Preset for this schedule</H>

<Preset src={presetData.icon} label={presetData.name} />

<H tag={6} thin>Days active</H>
<div class="flex flex-wrap justify-center gap-2">
  {#each JSON.parse(scheduleData.activeDays || "[]") as day}
    <Button size="small">{day}</Button>
  {/each}
</div>

<H tag={6} thin>Hours active</H>
<H tag={4} className="-mt-2" thin
  >{stringTimeFromNumber(scheduleData.startTime)} - {stringTimeFromNumber(scheduleData.stopTime)}</H
>

<H tag={6} thin>Status</H>
<div class="grayscale">
  <Button isWarning>Not running</Button>
</div>

<DangerZone deleteUrl="/focus/editschedule/delete" label="Delete Schedule" />
