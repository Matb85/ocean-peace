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
  import SM from "$lib/sessionManager";

  let allPresets: PresetI[] = [];
  let allSchedules: ScheduleI[] = [];
  onMount(async () => {
    allPresets = await Api.getAllPresets();
    allSchedules = await Api.getAllSchedules();
  });

  /** sets proper session data before leaving the page
   * @returns {void}
   */
  function beforeAddPreset() {
    SM.preset.setProps({
      id: "preset" + Date.now(),
      name: "",
      icon: "",
    });

    SM.action.setProps({ type: "Add", backUrl: "/focus", continueUrl: "/focus/editpreset/1" });
    SM.dialogs.setProps({ apps: "[]", websites: "[]" });
  }
  /** sets proper session data before leaving the page
   * @returns {void}
   */
  function beforeAddSchedule() {
    console.error("beforeAddSchedule hello");
    SM.schedule.setProps({
      id: "schedule" + Date.now(),
      name: "",
      preset: allPresets[0].id,
      activeDays: "[]",
      startTime: 60 * 16.5 + "",
      stopTime: 60 * 18.5 + "",
    });
    SM.action.setProps({ type: "Add", backUrl: "/focus", continueUrl: "/focus/editschedule/1" });
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
    <Link href="/focus/editpreset/1" on:click={beforeAddPreset}>
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
  <Link href="/focus/editschedule/1" on:click={beforeAddSchedule}>
    <Button secondary>Add a Schedule</Button>
  </Link>
</div>
