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
  import Link from "$lib/Link.svelte";

  let allPresets: PresetI[] = [];
  let allSchedules: ScheduleI[] = [];
  onMount(async () => {
    allPresets = await Api.getAllPresets();
    allSchedules = await Api.getAllSchedules();
  });

  import SM from "$lib/sessionManager";

  /** set proper session data before leaving the page
   * @param to the path
   * @returns nothing
   */
  function beforeNavigate(to: string) {
    if (to == "/focus/editpreset/1") {
      SM.preset.id = "" + Date.now();
      SM.preset.name = "";
      SM.preset.icon = "";

      SM.action.type = "Add";
      SM.action.backUrl = "/focus";
      SM.action.continueUrl = "/focus/editpreset/1";

      SM.dialogs.apps = "[]";
      SM.dialogs.websites = "[]";
    } else if (to == "/focus/editschedule/1") {
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

    beforeNavigate("/focus/editschedule/1");
    beforeNavigate("/focus/editpreset/1");
  }
</script>

<FullHeading backHref="/">Focus</FullHeading>

<H thin>Presets</H>

<div class="grid grid-cols-2 gap-4">
  {#each allPresets as preset}
    <Link href="/focus/preset?id={preset.id}">
      <Preset src={preset.icon} label={preset.name} />
    </Link>
  {/each}
  {#each new Array(Math.abs(4 - allPresets.length)) as _}
    <Link href="/focus/editpreset/1" on:click={() => beforeNavigate("/focus/editpreset/1")}>
      <Preset src={A} noShadowWrapper />
    </Link>
  {/each}
</div>

<H thin>Schedule</H>

<div class="card-flex-col">
  {#each allSchedules as schedule}
    <Link className="w-full" href="/focus/schedule?id={schedule.id}">
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
    </Link>
  {/each}
  {#if allSchedules.length == 0}
    <p>No schedules</p>
  {/if}
  <Link href="/focus/editschedule/1" on:click={() => beforeNavigate("/focus/editschedule/1")}>
    <Button secondary>Add a Schedule</Button>
  </Link>
</div>
