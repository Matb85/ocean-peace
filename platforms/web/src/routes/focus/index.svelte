<script lang="ts">
  import { Button } from "@redinnlabs/system/Elements";
  import { Schedule, Preset } from "@redinnlabs/system/Units";
  import A from "@redinnlabs/system/assets/icon-add.svg";
  import FullHeading from "$lib/FullHeading.svelte";
  import H from "$lib/H.svelte";
  import { stringTimeFromNumber } from "$lib/utils";
  import { onMount } from "svelte";
  import type { PresetI, ScheduleI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";

  let allPresets: PresetI[] = [];
  let allSchedules: ScheduleI[] = [];
  onMount(async () => {
    allPresets = await Api.getAllPresets();
    allSchedules = await Api.getAllSchedules();
  });

  import SM from "$lib/sessionManager";
  import { beforeNavigate } from "$app/navigation";

  beforeNavigate(({ to }) => {
    if (to.pathname == "/focus/editpreset/1") {
      SM.preset.id = "" + Date.now();
      SM.preset.name = "";
      SM.preset.icon = "";

      SM.action.type = "Add";
      SM.action.backUrl = "/focus";
      SM.action.continueUrl = "/focus/editpreset/1";

      SM.dialogs.apps = "[]";
      SM.dialogs.websites = "[]";
    } else if (to.pathname == "/focus/editschedule/1") {
      SM.schedule.id = "" + Date.now();
      SM.schedule.name = "";
      SM.schedule.preset = "";
      SM.schedule.activeDays = "[]";
      SM.schedule.startTime = 60 * 16.5 + "";
      SM.schedule.stopTime = 60 * 18.5 + "";

      SM.action.type = "Add";
      SM.action.backUrl = "/focus";
      SM.action.continueUrl = "/focus/editschedule/1";
    }
  });
</script>

<FullHeading backHref="/">Focus</FullHeading>

<H thin>Presets</H>

<div class="grid grid-cols-2 gap-4">
  {#each allPresets as preset}
    <a sveltekit:prefetch href="/focus/preset?id={preset.id}">
      <Preset src={preset.icon} label={preset.name} />
    </a>
  {/each}
  {#each new Array(Math.abs(4 - allPresets.length)) as _}
    <a sveltekit:prefetch href="/focus/editpreset/1">
      <Preset src={A} noShadowWrapper />
    </a>
  {/each}
</div>

<H thin>Schedule</H>

<div class="card-flex-col">
  {#each allSchedules as schedule}
    <a class="w-full" sveltekit:prefetch href="/focus/schedule?id={schedule.id}">
      <Schedule
        alt=""
        src={allPresets.filter(x => (x.id = schedule.preset))[0].icon}
        title={schedule.name}
        info={JSON.parse(schedule.activeDays).join(",") +
          " | " +
          stringTimeFromNumber(schedule.startTime) +
          "-" +
          stringTimeFromNumber(schedule.stopTime)}
      />
    </a>
  {/each}
  {#if allSchedules.length == 0}
    <p>No schedules</p>
  {/if}
  <a sveltekit:prefetch href="/focus/editschedule/1">
    <Button secondary>Add a Schedule</Button>
  </a>
</div>
