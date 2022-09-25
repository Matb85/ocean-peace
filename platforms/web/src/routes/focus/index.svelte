<script lang="ts">
  import { Button, H } from "@redinnlabs/system/Elements";
  import { Schedule, Preset } from "@redinnlabs/system/Units";
  import A from "@redinnlabs/system/assets/icon-add.svg";
  import FullHeading from "$lib/FullHeading.svelte";

  import { stringTimeFromNumber } from "$lib/utils";
  import { onMount } from "svelte";
  import type { PresetI, ScheduleI } from "$schema";
  import Api from "@redinn/oceanpeace-mobile/api";
  import Link from "$lib/Link.svelte";
  import SM from "$lib/sessionManager";
  import { t } from "$lib/i18n";

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

    SM.action.setProps({ type: "add", backUrl: "/focus", continueUrl: "/focus/editpreset/1" });
    SM.dialogs.setProps({ apps: "[]", websites: "[]" });
  }
  /** sets proper session data before leaving the page
   * @param i the index of the Preset in the array of all the Preset
   * @returns {void}
   */
  function beforeOpenPreset(i: number) {
    SM.preset.setProps({
      id: allPresets[i].id,
      name: allPresets[i].name,
      icon: allPresets[i].icon,
    });
    SM.dialogs.setProps({
      apps: allPresets[i].apps,
      websites: allPresets[i].websites,
    });
  }
  /** sets proper session data before leaving the page
   * @returns {void}
   */
  function beforeAddSchedule() {
    console.error("dlkfjslkfjslkfjskldjfslkj");
    SM.schedule.setProps({
      id: "schedule" + Date.now(),
      name: "",
      preset: allPresets[0].id,
      activeDays: "[]",
      startTime: 60 * 16.5 + "",
      stopTime: 60 * 18.5 + "",
    });
    SM.action.setProps({ type: "add", backUrl: "/focus", continueUrl: "/focus/editschedule/1" });
  }
  /** sets proper session data before leaving the page
   * @param i the index of the schedule in the array of all the schedules
   * @returns {void}
   */
  function beforeOpenSchedule(i: number) {
    SM.schedule.setProps({
      id: allSchedules[i].id,
      name: allSchedules[i].name,
      preset: allSchedules[i].preset,
      activeDays: allSchedules[i].activeDays,
      startTime: allSchedules[i].startTime,
      stopTime: allSchedules[i].stopTime,
    });
  }
</script>

<FullHeading tag={3} backHref="/">{$t("d.focus.focus")}</FullHeading>

<H thin>{$t("d.focus.presets")}</H>

<div class="grid grid-cols-2 gap-4">
  {#each allPresets as preset, i}
    <Link href="/focus/preset?id={preset.id}" on:click={() => beforeOpenPreset(i)}>
      <Preset src={preset.icon} label={preset.name} />
    </Link>
  {/each}
  {#each new Array(Math.abs(4 - allPresets.length)) as _}
    <Link href="/focus/editpreset/1" on:click={beforeAddPreset}>
      <Preset src={A} noShadowWrapper />
    </Link>
  {/each}
</div>

<H thin>{$t("d.schedule.schedule")}</H>

<div class="card-flex-col">
  {#each allSchedules as schedule, i}
    <Link className="w-full" href="/focus/schedule?id={schedule.id}" on:click={() => beforeOpenSchedule(i)}>
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
    <p>{$t("d.schedule.none")}</p>
  {/if}
  <Link
    href={allPresets.length > 0 ? "/focus/editschedule/1" : ""}
    className={allPresets.length > 0 ? "" : "grayscale"}
    on:click={beforeAddSchedule}
  >
    <Button secondary>{$t("d.schedule.add")}</Button>
  </Link>
</div>
